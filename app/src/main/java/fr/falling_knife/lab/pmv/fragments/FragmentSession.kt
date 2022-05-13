package fr.falling_knife.lab.pmv.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.falling_knife.lab.pmv.R
import fr.falling_knife.lab.pmv.utils.DataApp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "_login"
private const val ARG_PARAM2 = "_password"
private const val ARG_PARAM3 = "_serverAddress"
private const val ARG_PARAM4 = "_serverPort"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentSession.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentSession : Fragment() {
    // TODO: Rename and change types of parameters
    private var _login: String? = null
    private var _password: String? = null
    private var _serverAddress: String? = null
    private var _serverPort: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _login = it.getString(ARG_PARAM1)
            _password = it.getString(ARG_PARAM2)
            _serverAddress = it.getString(ARG_PARAM3)
            _serverPort = it.getString(ARG_PARAM4)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session, container, false)
        // TODO: Appel listener onSession
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentSession.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(login: String, password: String, serverAddress: String, serverPort: String) =
            FragmentSession().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, login)
                    putString(ARG_PARAM2, password)
                    putString(ARG_PARAM3, serverAddress)
                    putString(ARG_PARAM4, serverPort)
                }
            }
    }

    interface OnSessionManagement{
        fun onSessionRunning(settings: DataApp)
        fun onEndSession(settings: DataApp)
    }
}