package fr.falling_knife.lab.pmv.utils

import android.util.Log
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*
import kotlin.collections.ArrayList

enum class RequestType{
    BUTTON,
    LOGIN,
    USER_SELECT,
    CONTROL
}

class CommunicationProtocol {

    fun prepareRequest(type: RequestType, datas: ArrayList<String>): String{
        var request: String = "{"+
                """"command": """
        when(type){
            RequestType.LOGIN -> request += """"authCheck",""" +
                    """"data": {""" +
                    """"login": "${datas[0]}",""" +
                    """"pass": "${datas[1]}"""" +
                    "}"
            RequestType.BUTTON -> request += """"btnState",""" +
                    """"data": {""" +
                    """"btnSess": ${datas[0]},""" +
                    """"btnPrep": ${datas[1]},""" +
                    """"btnAVM:" ${datas[2]},""" +
                    """"btnReady": ${datas[3]},""" +
                    """"btnGo": ${datas[4]}""" +
                    "}"
            RequestType.USER_SELECT -> request += """"transfertRunner",""" +
                    """"data": {""" +
                    """"id": ${datas[0]},""" +
                    """"runners": [""" +
                    "${datas[1]}, ${datas[2]}" +
                    "]" +
                    "}"
            RequestType.CONTROL -> request += """"getControl""""
        }

        request += "}"

        return request
    }

    fun decodeData(data: String): ArrayList<Any>{
        Log.d("decodeData1", data)
        var command: String
        var jsonObject: JSONObject
        try{
            jsonObject = JSONTokener(data).nextValue() as JSONObject
            command = jsonObject.getString("command")
            Log.d("decodeData2", command)
        }catch(e: Exception){
            Log.d("decodeData2E", data)
            jsonObject = JSONTokener("{\"command\": \"NO JSON\"}").nextValue() as JSONObject
            command = jsonObject.getString("command")
        }

        return when(command){
            "authCheck" -> {
                val jsonData = jsonObject.getJSONObject("data")
                Log.d("decodeData3", jsonData.toString())
                val authStatus = jsonData.getInt("success")
                Log.d("decodeData4", authStatus.toString())
                if(authStatus == 1) arrayListOf("authTrue") else arrayListOf("authFalse")
            }
            "getControl" -> {
                Log.d("decodeData5", "getControl")
                arrayListOf("getControl")
            }
            "transfertAllRunners" -> {
                Log.d("decodeData7", "transfertAllRunners")
                val runners: ArrayList<Any> = decodeTransfertAllRunners(jsonObject.getJSONObject("data")) as ArrayList<Any>
                runners.add(0, "transfertAllRunners")
                runners
            }
            "sessionTransfert" -> {
                Log.d("Prot::decodeData", "sessionTransfert")
                val session: ArrayList<Any> = decodeSessionTransfert(jsonObject.getJSONObject("data")) as ArrayList<Any>
                session.add(0, "sessionTransfert")
                session
            }
            "btnState" -> {
                Log.d("Prot::decodeData", "btnState")
                val btnState: ArrayList<Any> = decodeBtn(jsonObject.getJSONObject("data")) as ArrayList<Any>
                btnState.add(0, "btnState")
                btnState
            }
            else -> {
                Log.d("decodeData6", "REC UNKNOWN")
                arrayListOf("UNKNOWN !")
            }
        }
    }

    private fun decodeTransfertAllRunners(data: JSONObject): ArrayList<String>{
        val count: Int = data.getInt("runnersCnt")
        val runners: ArrayList<String> = arrayListOf()

        runners.add(data.getString("sessionName"))
        for(i in 1..count){
            runners.add(data.getJSONObject("runner$i").getString("name"))
        }
        return runners
    }

    private fun decodeSessionTransfert(data: JSONObject): ArrayList<ArrayList<String>>{
        val count: Int = data.getInt("runCnt")
        val runs: ArrayList<ArrayList<String>> = arrayListOf()
        for(i in 1..count){
            val currentRun = data.getJSONObject("run$i")
            val run: ArrayList<String> = arrayListOf(
                currentRun.getString("runId"),
                currentRun.getString("runner1"),
                currentRun.getString("time1"),
                currentRun.getString("wind1"),
                currentRun.getString("speed1"),
                currentRun.getString("runner2"),
                currentRun.getString("time2"),
                currentRun.getString("wind2"),
                currentRun.getString("speed2"))
            runs.add(run)
        }
        return runs
    }

    private fun decodeBtn(data: JSONObject): ArrayList<Int>{
        return arrayListOf(
            data.getInt("btnSess"),
            data.getInt("btnPrep"),
            data.getInt("btnAVM"),
            data.getInt("btnReady"),
            data.getInt("btnGo"),
            data.getInt("btnStop")
        )
    }

}