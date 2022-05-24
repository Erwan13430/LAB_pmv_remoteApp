package fr.falling_knife.lab.pmv

import android.os.Bundle
import android.widget.Button
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
        if(_protocol.decodeData(response)[1] == "authTrue"){
            message = "Connected to ${settings.SERVER_ADDRESS} Port=${settings.SERVER_PORT}"
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, FragmentSession.newInstance(
                settings.login, settings.password, settings.SERVER_ADDRESS, settings.SERVER_PORT.toString()
            ), "FrgmSess").commit()
        } // if
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSessionRunning(settings: DataApp) {
        _client.alwaysReadFromServer()
        // TODO Not yet implemented
    }

    override fun onEndSession(settings: DataApp) {
        // TODO Not yet implemented
        // TODO Ajouter fonction fermeture socket
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
                f.disableInterface()
            }
            ReceiveActions.RUNNERS -> {
                f.setRunnersList(data)
            }
            ReceiveActions.SESSION -> {
                f.restoreSession(data)
            }
        }
    }

}