package fr.falling_knife.lab.pmv.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.io.OutputStream
import java.net.Inet4Address
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class WebService : Service() {


    val address = "192.168.7.100"
    val port = 55255
    lateinit var socket: Client
    val binder: IBinder = LocalWebBinder()
    override fun onCreate() {
        super.onCreate()
        socket = Client(address, port)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        socket.run()
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return this.binder
    }

    override fun onDestroy() {
        socket.delete()
        super.onDestroy()
    }
    inner class LocalWebBinder: Binder(){
        fun getService(): WebService{
            return this@WebService
        }
    }

}

class Client(address: String, port: Int){
    private val connection: Socket = Socket(address, port)
    private var connected: Boolean = true

    init {
        println("Connected to server at $address on port $port")
    }

    private val reader: Scanner = Scanner(connection.getInputStream())
    private val writer: OutputStream = connection.getOutputStream()

    fun run(){
        thread{
            read()
        }
    }

    fun delete(){
        reader.nextLine()
        connection.close()
        reader.close()
        writer.close()
        connected = false
    }

    private fun write(message: String){
        writer.write(message.toByteArray(Charset.defaultCharset()))
    }

    private fun read(){
        while(connected){
            var data: String = reader.nextLine()
            println(data)

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
    }

    private fun prepareJson(data: ArrayList<String>): String {
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