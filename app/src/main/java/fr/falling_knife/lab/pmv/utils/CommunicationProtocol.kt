package fr.falling_knife.lab.pmv.utils


import org.json.JSONObject
import org.json.JSONTokener
import java.util.*

class CommunicationProtocol(address: String, port: Int){

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
}