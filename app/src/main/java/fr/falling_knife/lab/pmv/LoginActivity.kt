package fr.falling_knife.lab.pmv

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.falling_knife.lab.pmv.utils.CommunicationProtocol
import java.io.*
import java.net.Socket
import java.util.*


class LoginActivity: AppCompatActivity(){


    private val SERVER_IP: String = "192.168.11.226"
    private val SERVER_PORT: Int = 2314
    private var threadMain: Thread? = null

    companion object{
        lateinit var output: PrintWriter
        var input: Scanner? = null
        lateinit var socket: Socket

        fun exitApp(){
            input?.close()
            output?.close()
            socket.close()
            return
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initLoginIHM()
        threadMain = Thread(ThreadConnect())
        threadMain?.start()
    }

    fun initLoginIHM(){
        val edtPass = findViewById<EditText>(R.id.edtLoginPasswd)
        edtPass.setImeActionLabel("Connexion", KeyEvent.KEYCODE_ENTER)
        edtPass.setOnKeyListener(View.OnKeyListener(){ v, keyCode, event ->
                if(event.action == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.KEYCODE_ENTER){
                    hideKeyboard()
                    onBtnLogin(v)
                    return@OnKeyListener true
                }
                false
        })
    }

    fun hideKeyboard(){
        val view: View? = this.currentFocus
        view?.let {
            val manager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun onBtnLogin(view: View){
        val info = Toast.makeText(this, "Vérification des identifiants...", Toast.LENGTH_LONG)
        info.show()
        authCheck(findViewById<EditText>(R.id.edtLoginId).text.toString(), findViewById<EditText>(R.id.edtLoginPasswd).text.toString())
    }

    fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun authCheck(login: String, pass: String){
        if(login.isEmpty()){
            println("The Login is Empty")
        }else if(pass.isEmpty()){
            println("The Password is Empty")
        }else{
            Thread(ThreadWrite(CommunicationProtocol.prepareJson(arrayListOf("authCheck", login, pass)))).start()
        }
    }


    inner class ThreadConnect: Runnable{
        override fun run(){
            try{
                socket = Socket(SERVER_IP, SERVER_PORT)
                output = PrintWriter(socket.getOutputStream())
                input = Scanner(socket.getInputStream())
                output.write("Connected\n")
                output.flush()
                Thread(ThreadRead()).start()
                runOnUiThread(Runnable {
                    fun run(){
                        Toast.makeText(this@LoginActivity, "Connected to Server\n", Toast.LENGTH_SHORT).show()
                    }
                })
            }catch(e: IOException){
                e.printStackTrace()
            }
        }
    }

    inner class ThreadRead: Runnable{
        override fun run() {
            while (true){
                try {
                    var readMessage: String = String()
                    readMessage = input!!.nextLine()
                        if (readMessage.isNotEmpty()) {
                            val nb = readMessage.length
                            if (nb > 0) {
                                println(readMessage.toString())
                                when(CommunicationProtocol.decodeData(readMessage.toString())){
                                    "authTrue" -> runOnUiThread(Runnable {
                                        startMainActivity()
                                    })
                                }
                                break
                            }
                        }//if
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    inner class ThreadWrite(message: String): Runnable{
        private var _message: String = message

        override fun run() {
            output?.let {
                output.write(_message)
                output.flush()
            }
        }
    }
}