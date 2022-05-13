package fr.falling_knife.lab.pmv.utils

import fr.falling_knife.lab.pmv.MainActivity
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

    fun authenticate(dataLogin: DataApp): String{
        val one = CoroutineScope(Dispatchers.IO).async {
            if(connectToServer(dataLogin))
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
            println(response)
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
            _socket != null
        }catch(e: Exception){
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
}