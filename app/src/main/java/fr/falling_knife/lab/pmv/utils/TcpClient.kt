package fr.falling_knife.lab.pmv.utils

import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class TcpClient(address: String, port: Int) {

    private val connection: Socket = Socket(address, port)
    private var connected: Boolean = true

    init {
        println("Connected to server at $address on port $port")
    }

    private val reader: Scanner = Scanner(connection.getInputStream())
    private val writer: OutputStream = connection.getOutputStream()

    fun run(){
        thread { read() }
    }

    private fun read(){
        while(connected)
            println(reader.nextLine())
    }

    fun sendCommand(cmd: String, args: ArrayList<String>){
        lateinit var data: String
        when(cmd){
            "transfertRunner" -> data = prepareJson(arrayListOf(cmd, args[0], args[1], args[2]))
            "getCSV" -> data = prepareJson(arrayListOf(cmd))
            "authCheck" -> data = prepareJson(arrayListOf(cmd, args[0], args[1]))
            "oldSessionExist" -> data = prepareJson(arrayListOf(cmd))
            "btnStartSession" -> data = prepareJson(arrayListOf(cmd, args[0]))
        }
        write(data)
    }

    fun prepareJson(data: ArrayList<String>): String {
        lateinit var trame: String
        when (data[0]) {
            "transfertRunner" -> trame = """{ "command": "${data[0]}", "data": { "id": "${data[1]}", "runners" : [ ${data[2].toInt()}, ${data[3].toInt()}] } } """
            "getCsv" -> trame = """{ "command": "${data[0]}"}"""
            "authCheck" -> trame = """{"command": "${data[0]}", "data":{"login" : "${data[1]}", "pass" : "${data[2]}"}}"""
            "oldSessionExist" -> trame = """{"command" : "${data[0]}"}"""
            "btnSession" -> trame = """{"command" : "${data[0]}", "data":{"value": ${data[1].toInt()}}}"""
        }
        return trame
    }

    private fun write(message: String){
        writer.write((message).toByteArray(Charset.defaultCharset()))
    }

    fun close(){
        connected = false
        reader.close()
        connection.close()
    }
}