package fr.falling_knife.lab.pmv.utils

data class DataApp(
    val login: String = "login",
    val password: String = "passwd",
    val SERVER_ADDRESS: String = "10.3.141.1",
    //val SERVER_ADDRESS: String = "82.64.219.20",
    val SERVER_PORT: Int = 2314,
    var message: String = "Nothing"
)

enum class SendAction{
    BUTTON,
    RUN,
    CSV,
    CONTROL
}

data class DataSend(
    val mode: SendAction,
    val data: ArrayList<String>
)

enum class ReceiveActions{
    CONTROL,
    BUTTON,
    RUNS, //Servira dans une version future de l'application
    RUNNERS,
    SESSION
}
