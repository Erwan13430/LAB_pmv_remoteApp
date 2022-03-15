package fr.falling_knife.lab.pmv

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

const val EXTRA_MESSAGE = "fr.falling_knife.lab.pmv.MESSAGE"

class LoginActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun initLoginIHM(){
        val edtPass = findViewById<EditText>(R.id.edtLoginPasswd)
        edtPass.setImeActionLabel("Connexion", KeyEvent.KEYCODE_ENTER)
        edtPass.setOnKeyListener(View.OnKeyListener(){ v, keyCode, event ->
                if(event.action == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.KEYCODE_ENTER){
                    onBtnLogin(v)
                    return@OnKeyListener true
                }
                false
        })
    }

    fun onBtnLogin(view: View){
        val info = Toast.makeText(this, "Vérification des identifiants...", Toast.LENGTH_LONG)
        info.show()

        if(findViewById<EditText>(R.id.edtLoginId).text.toString() == "LAB"){
            if(findViewById<EditText>(R.id.edtLoginPasswd).text.toString() == "Pass") {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(EXTRA_MESSAGE, "Coucou")
                }
                startActivity(intent)
            }else{
                Toast.makeText(this, "Mot de passe incorrect!", Toast.LENGTH_SHORT).show()
                findViewById<EditText>(R.id.edtLoginPasswd).text.clear()
            }
        }else{
            Toast.makeText(this, "Identifiant incorrect!", Toast.LENGTH_SHORT).show()
            findViewById<EditText>(R.id.edtLoginId).text.clear()
        }
    }
}