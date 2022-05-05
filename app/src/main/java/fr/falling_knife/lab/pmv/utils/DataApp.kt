package fr.falling_knife.lab.pmv.utils

data class DataApp(
    val login: String,
    val password: String,
    val SERVER_ADDRESS: String = "192.168.1.1",
    val SERVER_PORT: Int = 2314,
    var message: String = "Nothing"
)
