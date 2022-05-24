package fr.falling_knife.lab.pmv.utils

import android.util.Log
import fr.falling_knife.lab.pmv.MainActivity
import fr.falling_knife.lab.pmv.R
import fr.falling_knife.lab.pmv.fragments.FragmentSession
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.Socket
import kotlin.Exception

class TcpClient(activity: MainActivity) {

    private var _activity: MainActivity = activity
    private var _socket: Socket? = null
    private lateinit var _writer: OutputStream
    private lateinit var _reader: InputStream
    private val _protocol = CommunicationProtocol()
    private val TIMEOUT: Long = 1000
    private var _endThread: Boolean = false
    private var _end: Boolean = false

    fun authenticate(dataLogin: DataApp): String{
        val one = CoroutineScope(Dispatchers.IO).async {
            val state: Boolean = connectToServer(dataLogin)
            Log.d("TcpCl::authen","Is connected: $state")
            if(state)
                login(dataLogin)
            else
                "CA MARCHE PO"
        }
        var response: String
        runBlocking {
            response = one.await()
        }
        return response
    }

    private fun login(dataLogin: DataApp): String{
        var response = "Nothing"
        try{
            val request = _protocol.prepareRequest(RequestType.LOGIN, arrayListOf(dataLogin.login, dataLogin.password))
            _writer.write(request.toByteArray())
            response = readWithTimeout(TIMEOUT)
        }catch (e: Exception){
            response = e.message.toString()
            Log.d("TcpCl::login", "response")
        }
        return response
    }

    private fun connectToServer(dataLogin: DataApp): Boolean{
        return try{
            if(_socket == null){
                _socket = Socket(dataLogin.SERVER_ADDRESS, dataLogin.SERVER_PORT)
                _writer = _socket!!.getOutputStream()
                _reader = _socket!!.getInputStream()
            }
            Log.d("TcpCl::connectServ","Socket is ${_socket != null}")
            true
        }catch(e: Exception){
            Log.d("TcpCl::connectServE","Socket is planted")
            false
        }
    }

    private fun readWithTimeout(time: Long): String{
        var response = "NOTHING"
        var tempo: Long = 0
        var delta: Long = 50
        val bReader = BufferedReader(InputStreamReader(_reader))
        while(tempo < time){
            val nb = _reader.available()
            if(nb > 0){
                response = bReader.readLine()
                break
            } //if
            runBlocking{
                delay(delta)
            } // runBlocking
            tempo += delta
        } // while
        if(tempo == time){
            response = "NO RESPONSE"
        } //if
        return response
    } //readWithTimeout

    fun alwaysReadFromServer(){
        _endThread = false
        _end = false
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO){
            while(!_end){
                val response = readWithTimeout(TIMEOUT)
                val lines = _protocol.decodeData(response)
                if(lines.isNotEmpty()){
                    _activity.runOnUiThread{
                        when(lines) {
                            "getControl" -> _activity?.onUpdateSession(ReceiveActions.CONTROL, arrayListOf())
                        }
                    }
                }
            }
            _endThread = true
        }
    }
}