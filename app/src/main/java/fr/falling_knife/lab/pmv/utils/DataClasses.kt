package fr.falling_knife.lab.pmv.utils

data class DataApp(
    val login: String = "login",
    val password: String = "passwd",
    val SERVER_ADDRESS: String = "192.168.13.226",
    val SERVER_PORT: Int = 4444,
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
    RUNS,
    RUNNERS,
    SESSION
}
