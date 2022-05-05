package fr.falling_knife.lab.pmv.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import fr.falling_knife.lab.pmv.R
import fr.falling_knife.lab.pmv.utils.DataApp
import java.lang.ClassCastException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "_serverAddress"
private const val ARG_PARAM2 = "_serverPort"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentLogin.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentLogin : Fragment() {
    // TODO: Rename and change types of parameters
    private var _serverAddress: String? = null
    private var _serverPort: String? = null
    private lateinit var listener: OnCheckConnectionSettings

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnCheckConnectionSettings){
            listener = context
        }else{
            throw ClassCastException("$context must implement params")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _serverAddress = it.getString(ARG_PARAM1)
            _serverPort = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val rootView = inflater.inflate(R.layout.fragment_login, container, false)

        if(savedInstanceState == null){
            val edtLogin = rootView.findViewById<EditText>(R.id.edtLoginId)
            val edtPass = rootView.findViewById<EditText>(R.id.edtLoginPasswd)
            val btnConnect = rootView.findViewById<Button>(R.id.btnLogin)

            btnConnect.setOnClickListener{
                Toast.makeText(activity?.baseContext, "Vérification des identifiants", Toast.LENGTH_SHORT).show()

                listener.onCheckConnectionSettings(
                    DataApp(edtLogin.text.toString(), edtPass.text.toString())
                )
            } // Listener
        }

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param serverAddress Parameter 1.
         * @param serverPort Parameter 2.
         * @return A new instance of fragment FragmentLogin.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(serverAddress: String, serverPort: String) =
            FragmentLogin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, serverAddress)
                    putString(ARG_PARAM2, serverPort)
                }
            }
    }

    interface OnCheckConnectionSettings{
        fun onCheckConnectionSettings(settings: DataApp)
    }
}