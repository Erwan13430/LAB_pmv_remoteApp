package fr.falling_knife.lab.pmv.utils

import android.util.Log
import org.json.JSONObject
import org.json.JSONTokener

enum class RequestType{
    BUTTON,
    LOGIN,
    USER_SELECT
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
        }

        request += "}"

        return request
    }

    fun decodeData(data: String): String{
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
                if(authStatus == 1) "authTrue" else "authFalse"
            }
            "getControl" -> {
                Log.d("decodeData5", "getControl")
                "getControl"
            }
            else -> {
                Log.d("decodeData6", "REC UNKNOWN")
                "UNKNOWN !"
            }
        }
    }

}