package fr.falling_knife.lab.pmv.utils

import android.os.AsyncTask
import fr.falling_knife.lab.pmv.LoginActivity
import org.json.JSONObject
import org.json.JSONTokener
import java.io.*
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread

class CommunicationProtocol(address: String, port: Int): Runnable{

    companion object{
        fun decodeData(data: String): String{
            val jsonObject = JSONTokener(data).nextValue() as JSONObject

            val command = jsonObject.getString("command")
             var returnVal: String = ""

            when(command){
                "authCheck" -> {
                    val jsonData = jsonObject.getJSONObject("data")
                    val authSuccess = jsonData.getInt("success")
                    //responseAuth = if(authSuccess == 0 ) false else true
                    returnVal = if(authSuccess == 0) "authFalse" else "authTrue"
                }
            }

                return returnVal
        }

        fun prepareJson(data: ArrayList<String>): String {
            var trame: String =
                "{" +
                        """"command" : "${data[0]}""""
            when (data[0]) {

                "transfertRunner" -> trame +=
                    "," +
                            """"data": {""" +
                            """"id": "${data[1]}",""" +
                            """"runners" : [""" +
                            "${data[2].toInt()}," +
                            "${data[3].toInt()}" +
                            "]" +
                            "}"

                "authCheck" -> trame +=
                    "," +
                            """"data":{""" +
                            """"login": "${data[1]}",""" +
                            """"pass": "${data[2]}",""" +
                            "}"

                "btnStartSession" -> trame +=
                    "," +
                            """"data":{""" +
                            """"value" : ${data[1]}""" +
                            "}"

                "btnPrep" -> trame +=
                    "," +
                            """"data":{""" +
                            """"value": ${data[1].toInt()}""" +
                            "}"

                "btnAVM" -> trame +=
                    "," +
                            """"data":{""" +
                            """"value": ${data[1].toInt()}""" +
                            "}"

            }

            trame += "}"

            return trame
        }
    }

    private val address = address
    private val port = port
    override fun run() {
    lateinit var socket: Socket
    lateinit var output: PrintWriter
    lateinit var input: BufferedWriter
        try{
            socket = Socket(address, port)
            output = PrintWriter(socket.getOutputStream())

        }catch(e: IOException){
            e.printStackTrace()
        }
    }

    /*
    private lateinit var connection: Socket
    val thread: Thread = Thread(){
        connection = Socket(address, port)
    }
    private var connected: Boolean = true

    var authStatus: Boolean = false
    var responseAuth: Boolean = false

    init {
        println("Connected to server at $address on port $port")
    }

    private lateinit var reader: Scanner
    private lateinit var writer: OutputStream
    val thread2 = Thread() {
        reader = Scanner(connection.getInputStream())
        writer = connection.getOutputStream()
    }


    fun run(){
        thread {
            read()
        }
    }

    private fun write(message: String){
        thread {
            if(this::writer.isInitialized)
                writer.write(message.toByteArray(Charset.defaultCharset()))
        }
    }

    private fun read(){
        thread {
            while(this::reader.isInitialized) {
                while (connected) {
                    println(reader.nextLine())
                }
            }
        }
    }

    fun sendMessage(cmd: String, args: ArrayList<String>){
        var data: String = when(cmd){
            "transfertRunner" -> prepareJson(arrayListOf(cmd, args[0], args[1], args[2]))
            "getCSV" -> prepareJson(arrayListOf(cmd))
            "authCheck" -> prepareJson(arrayListOf(cmd, args[0], args[1]))
            "oldSessionExist" -> prepareJson(arrayListOf(cmd))
            "btnStartSession" -> prepareJson(arrayListOf(cmd, args[0]))
            "btnPrep" -> prepareJson(arrayListOf(cmd, args[0]))
            "btnAVM" -> prepareJson(arrayListOf(cmd, args[0]))
            else -> return
        }
        write(data)
    }*/



    /*fun close(){
        reader.nextLine()
        connection.close()
        reader.close()
        writer.close()
        connected = false
    }*/

    private fun decodeData(data: String){
        val jsonObject = JSONTokener(data).nextValue() as JSONObject

        val command = jsonObject.getString("command")

        when(command){
            "authCheck" -> {
                val jsonData = jsonObject.getJSONObject("data")
                val authSuccess = jsonData.getInt("success")
                //responseAuth = if(authSuccess == 0 ) false else true
            }
        }
    }



}

/*class TcpThread2(): Runnable{

    companion object{
        lateinit var message: String
    }

    override fun run() {
        while(true){
            try{
                message = input.readLine();
                if(message != null)
            }
        }
    }

}*/