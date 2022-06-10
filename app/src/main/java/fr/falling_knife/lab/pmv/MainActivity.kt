package fr.falling_knife.lab.pmv

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.falling_knife.lab.pmv.fragments.FragmentLogin
import fr.falling_knife.lab.pmv.fragments.FragmentSession
import fr.falling_knife.lab.pmv.utils.*

class MainActivity: AppCompatActivity(), FragmentLogin.OnCheckConnectionSettings,
    FragmentSession.OnSessionManagement {

    /* Initialisation du client TCP */
    private val _client: TcpClient = TcpClient(this)
    private val _protocol: CommunicationProtocol = CommunicationProtocol()

    /* Mise en place du fond de l'interface */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
    }

    /* Vient du fragment FragmentLogin */
    override fun onCheckConnectionSettings(settings: DataApp) {
        var message = "Unable to connect to ${settings.SERVER_ADDRESS} Port=${settings.SERVER_PORT}"
        val response = _client.authenticate(settings)
        val authState = _protocol.decodeData(response)[0]
        if(authState == "authNoSess"){
            message = "The session is not started on ${settings.SERVER_ADDRESS}:${settings.SERVER_PORT}"
            _client.endSession()
        }else if(authState == "authTrue"){
            message = "Connected to ${settings.SERVER_ADDRESS} Port=${settings.SERVER_PORT}"
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, FragmentSession.newInstance(
                settings.login, settings.password, settings.SERVER_ADDRESS, settings.SERVER_PORT.toString()
            ), "FrgmSess").commit()
        } else if(authState == "authFalse"){
            message = "Identifiant ou Mot de passe Incorrect"
            _client.endSession()
        } else {
            _client.endSession()
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSessionRunning(settings: DataApp) {
        _client.alwaysReadFromServer()
    }

    override fun onEndSession() {
        _client.endSession()
        (supportFragmentManager.findFragmentByTag("FrgmSess") as FragmentSession).endSession()
    }

    override fun onSendCommand(data: DataSend) {
        val trame = when(data.mode){
            SendAction.BUTTON -> _protocol.prepareRequest(RequestType.BUTTON, data.data)
            SendAction.CONTROL -> _protocol.prepareRequest(RequestType.CONTROL, data.data)
            else -> null
        }

        _client.sendCommand(trame!!)

        // TODO Not yet implemented
    }

    override fun onUpdateSession(type: ReceiveActions, data: ArrayList<Any>) {
        val f: FragmentSession = supportFragmentManager.findFragmentByTag("FrgmSess") as FragmentSession
        when(type) {
            ReceiveActions.CONTROL -> {
                f.disableInterfaceGC()
            }
            ReceiveActions.RUNNERS -> {
                f.setRunnersList(data)
            }
            ReceiveActions.SESSION -> {
                f.restoreSession(data)
            }
            ReceiveActions.BUTTON ->
                f.controlButtons(data)
            else -> {}
        }
    }

}