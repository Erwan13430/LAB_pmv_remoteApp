package fr.falling_knife.lab.pmv.utils

data class DataApp(
    val login: String = "login",
    val password: String = "passwd",
    val SERVER_ADDRESS: String = "192.168.4.31",
    val SERVER_PORT: Int = 4444,
    var message: String = "Nothing"
)
