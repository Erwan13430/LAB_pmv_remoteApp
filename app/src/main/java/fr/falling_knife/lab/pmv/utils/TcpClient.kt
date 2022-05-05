package fr.falling_knife.lab.pmv.utils

import fr.falling_knife.lab.pmv.MainActivity
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.lang.Exception
import java.net.Socket

class TcpClient(activity: MainActivity) {

    private var _activity: MainActivity = activity
    private lateinit var _socket: Socket
    private lateinit var _writer: OutputStream
    private lateinit var _reader: InputStream
    private val _protocol = CommunicationProtocol()
    private val TIMEOUT: Long = 1000

    fun authenticate(dataLogin: DataApp): String{
        val one = CoroutineScope(Dispatchers.IO).async {
            login(dataLogin)
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
            connectToServer(dataLogin)
            _writer.write(request.toByteArray())
            response = readWithTimeout(TIMEOUT)
            _socket.close()
        }catch (e: Exception){
            response = e.message.toString()
            println(response)
        }
        return response
    }

    private fun connectToServer(dataLogin: DataApp){
        _socket = Socket(dataLogin.SERVER_ADDRESS, dataLogin.SERVER_PORT)
        _writer = _socket.getOutputStream()
        _reader = _socket.getInputStream()
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