package fr.falling_knife.lab.pmv

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.falling_knife.lab.pmv.fragments.FragmentLogin
import fr.falling_knife.lab.pmv.fragments.FragmentSession
import fr.falling_knife.lab.pmv.utils.CommunicationProtocol
import fr.falling_knife.lab.pmv.utils.DataApp
import fr.falling_knife.lab.pmv.utils.TcpClient

class MainActivity: AppCompatActivity(), FragmentLogin.OnCheckConnectionSettings {

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
        if(_protocol.decodeData(response) == "authTrue"){
            message = "Connected to ${settings.SERVER_ADDRESS} Port=${settings.SERVER_PORT}"
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, FragmentSession.newInstance(
                settings.login, settings.password, settings.SERVER_ADDRESS, settings.SERVER_PORT.toString()
            )).commit()
        } // if
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}