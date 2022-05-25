package fr.falling_knife.lab.pmv.fragments

import android.app.StatusBarManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import fr.falling_knife.lab.pmv.R
import fr.falling_knife.lab.pmv.utils.DataApp
import fr.falling_knife.lab.pmv.utils.DataSend
import fr.falling_knife.lab.pmv.utils.ReceiveActions
import fr.falling_knife.lab.pmv.utils.SendAction

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
    private lateinit var _listener: OnSessionManagement
    private lateinit var _rootView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _listener = if(context is OnSessionManagement) context else let {
            throw ClassCastException("$context must implement params")
        }
    }

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
        _rootView = inflater.inflate(R.layout.fragment_session, container, false)

        if(savedInstanceState == null){
            //Initialisation de l'IHM

            //Récupération des boutons de gestion
            val btnStartSess: Button = _rootView.findViewById(R.id.btnSelect)
            val btnPrep: Button = _rootView.findViewById(R.id.btnPrep)
            val btnAvm: Button = _rootView.findViewById(R.id.btnAVM)
            val btnReady: Button = _rootView.findViewById(R.id.btnReady)
            val btnGo: Button = _rootView.findViewById(R.id.btnGo)
            val btnGest: ArrayList<Button> = arrayListOf(btnStartSess, btnPrep, btnAvm, btnReady, btnGo)
            val btnControl: Button = _rootView.findViewById(R.id.btnControl)


            //Initialisation des boutons de gestion
            btnStartSess.setOnClickListener { btnSelectListener(btnGest) }
            btnPrep.setOnClickListener{ btnPreparationListener(btnGest) }
            btnAvm.setOnClickListener{ btnAVMListener(btnGest) }
            btnReady.setOnClickListener{ btnReadyListener(btnGest) }
            btnGo.setOnClickListener { btnGoListener(btnGest) }
            btnControl.setOnClickListener { btnControlListener() }
            btnControl.isVisible = false

            //Récupération et initialisation des boutons radio
            val rdBtns: ArrayList<RadioButton> = getSelectButton(_rootView)
            initRadioButtons(rdBtns)

            initIHM(btnGest, rdBtns)

        } //if

        Log.d("FragmentSession", "Starting onSessionRunning")
        _listener.onSessionRunning(DataApp(_login!!, _password!!, _serverAddress!!, _serverPort!!.toInt(), "onSessionRunning"))
        return _rootView

        // TODO: Appel listener onSession
    }

    private fun initIHM(buttons: ArrayList<Button>, rdBtns: ArrayList<RadioButton>){
        for(i in 1 until buttons.count()){
            buttons[i].isEnabled = false
        }
        rdBtns[0].isChecked = true
        val text = String.format(resources.getString(R.string.lineNumber), "1")
        _rootView.findViewById<TextView>(R.id.lineTitle).text = text
        _rootView.findViewById<TextView>(R.id.txtSessionName).text = String.format(resources.getString(R.string.sessionName), resources.getString(R.string.defaultSessionName))
    }

    private fun getSelectButton(view: View): ArrayList<RadioButton>{
        val radios = arrayListOf<RadioButton>()

        for(i in 1..20 step 1)
            radios.add(view.findViewById(resources.getIdentifier("rdBtn$i", "id", activity?.packageName)))
        return radios
    }

    private fun getSpinners(view: View): ArrayList<Spinner>{
        val spinner = arrayListOf<Spinner>()

        for(i in 1..40 step 1)
            spinner.add(view.findViewById(resources.getIdentifier("spinRun$i", "id", activity?.packageName)))

        return spinner
    }

    private fun initSpinners(spinners: ArrayList<Spinner>, adapter: ArrayAdapter<String>){
        spinners.iterator().forEach{
            it.adapter = adapter
        }
    }

    private fun btnSelectListener(btnGest: ArrayList<Button>){
        Toast.makeText(activity?.baseContext, "Session démarrée", Toast.LENGTH_SHORT).show()
        for(i in 0 until btnGest.count()) {
            btnGest[i].isEnabled = i == 1
        }
    }

    private fun btnControlListener(){
        enableInterface()
    }

    private fun btnPreparationListener(btnGest: ArrayList<Button>){
        Toast.makeText(activity?.baseContext, "Préparation", Toast.LENGTH_SHORT).show()

        for(i in 0 until btnGest.count())
            btnGest[i].isEnabled = i == 2
    }

    private fun btnAVMListener(btnGest: ArrayList<Button>){
        Toast.makeText(activity?.baseContext, "À vos marques", Toast.LENGTH_SHORT).show()

        for(i in 0 until btnGest.count())
            btnGest[i].isEnabled = i == 1 || i == 3
    }

    private fun btnReadyListener(btnGest: ArrayList<Button>){
        Toast.makeText(activity?.baseContext, "Prêt", Toast.LENGTH_SHORT).show()

        for(i in 0 until btnGest.count())
            btnGest[i].isEnabled = i == 2 || i == 4
    }

    private fun btnGoListener(btnGest: ArrayList<Button>){
        if(btnGest[4].text == resources.getText(R.string.btnGo)) {
            for(i in 0 until btnGest.count())
                btnGest[i].isEnabled = i == 4
            btnGest[4].text = resources.getText(R.string.btnStop)
            Toast.makeText(activity?.baseContext, "Partez !", Toast.LENGTH_SHORT).show()
        }else{
            for(i in 0 until btnGest.count())
                btnGest[i].isEnabled = i == 1
            btnGest[4].text = resources.getText(R.string.btnGo)
            Toast.makeText(activity?.baseContext, "Arrêt de la course !", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRadioButtons(rdBtns: ArrayList<RadioButton>){
        for(i in 0 until rdBtns.count()){
            rdBtns[i].setOnClickListener{
                rdButtonListener(rdBtns, it)
            }
        }
    }

    private fun rdButtonListener(rdBtns: ArrayList<RadioButton>, radio: View){
        var id: Int = 0
        for(i in 0 until rdBtns.count())
            id = if (id == 0) {
                if (radio == rdBtns[i]) i + 1 else 0
            } else {
                id
            }

        val lineText = activity?.findViewById<TextView>(R.id.lineTitle)
        val text: String = String.format(resources.getString(R.string.lineNumber), id)
        lineText?.text = text
        Log.d("FrgSess::ID", id.toString())
        disableButtons(id, rdBtns)
    }

    private fun disableButtons(id: Int, radios: ArrayList<RadioButton>){
        radios.iterator().forEach {
            it.isChecked = it == radios[id-1]
            Log.d("FrgSess::Btn", "$it is ${it.isChecked}")
        }
    }

    fun disableInterface(){
        getSelectButton(_rootView).iterator().forEach {
            it.isEnabled = false
        }
        getSpinners(_rootView).iterator().forEach {
            it.isEnabled = false
        }

        _rootView.findViewById<Button>(R.id.btnSelect).isEnabled = false
        _rootView.findViewById<Button>(R.id.btnPrep).isEnabled = false
        _rootView.findViewById<Button>(R.id.btnAVM).isEnabled = false
        _rootView.findViewById<Button>(R.id.btnReady).isEnabled = false
        _rootView.findViewById<Button>(R.id.btnGo).isEnabled = false

        stateControlButton(true)
        _rootView.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarSelect).setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.teal_700))
        _rootView.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarControl).setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.teal_700))
        activity?.window?.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.teal_700)
    }

    private fun enableInterface(){
        getSelectButton(_rootView).iterator().forEach {
            it.isEnabled = true
        }
        getSpinners(_rootView).iterator().forEach {
            it.isEnabled = true
        }

        _rootView.findViewById<Button>(R.id.btnSelect).isEnabled = true
        _rootView.findViewById<Button>(R.id.btnPrep).isEnabled = true
        _rootView.findViewById<Button>(R.id.btnAVM).isEnabled = true
        _rootView.findViewById<Button>(R.id.btnReady).isEnabled = true
        _rootView.findViewById<Button>(R.id.btnGo).isEnabled = true

        stateControlButton(false)
        _rootView.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarSelect).setBackgroundColor(resources.getColor(R.color.red_200))
        _rootView.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarControl).setBackgroundColor(resources.getColor(R.color.red_200))
        activity?.window?.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.red_200)

        _listener.onSendCommand(DataSend(SendAction.CONTROL, arrayListOf()))

    }

    private fun stateControlButton(state: Boolean){
        _rootView.findViewById<Button>(R.id.btnControl).isVisible = state
    }

    fun setRunnersList(runners: ArrayList<Any>){
        val runnersList: ArrayList<String> = runners as ArrayList<String>
        _rootView.findViewById<TextView>(R.id.txtSessionName).text = String.format(resources.getString(R.string.sessionName), runnersList[0])
        runnersList.removeAt(0)
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(_rootView.context, android.R.layout.simple_spinner_dropdown_item, runnersList)
        initSpinners(getSpinners(_rootView), arrayAdapter)
    }

    fun restoreSession(data: ArrayList<Any>){
        val session: ArrayList<ArrayList<String>> = data as ArrayList<ArrayList<String>>
        for(i in 1..session.count()){
            val id: Int = session[i - 1][0].toInt()
            var spinner = _rootView.findViewById<Spinner>(resources.getIdentifier("spinRun${(i * 2) - 1}", "id", activity?.packageName))
            var adapt = spinner.adapter as ArrayAdapter<String>
            spinner.setSelection(adapt.getPosition(session[i - 1][1]))
            _rootView.findViewById<TextView>(resources.getIdentifier("txtTime${(i * 2) - 1}", "id", activity?.packageName)).text = session[i - 1][2]
            _rootView.findViewById<TextView>(resources.getIdentifier("txtWind${(i * 2) - 1}", "id", activity?.packageName)).text = session[i - 1][3]
            _rootView.findViewById<TextView>(resources.getIdentifier("txtSpeed${(i * 2) - 1}", "id", activity?.packageName)).text = session[i - 1][4]

            spinner = _rootView.findViewById<Spinner>(resources.getIdentifier("spinRun${i * 2}", "id", activity?.packageName))
            adapt = spinner.adapter as ArrayAdapter<String>
            spinner.setSelection(adapt.getPosition(session[i - 1][5]))
            _rootView.findViewById<TextView>(resources.getIdentifier("txtTime${i * 2}", "id", activity?.packageName)).text = session[i - 1][6]
            _rootView.findViewById<TextView>(resources.getIdentifier("txtWind${i * 2}", "id", activity?.packageName)).text = session[i - 1][7]
            _rootView.findViewById<TextView>(resources.getIdentifier("txtSpeed${i * 2}", "id", activity?.packageName)).text = session[i - 1][8]
        }
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
                }//arguments
            }//newInstance()
    } //companion object

    interface OnSessionManagement{
        fun onSessionRunning(settings: DataApp)
        fun onSendCommand(data: DataSend)
        fun onEndSession(settings: DataApp)
        fun onUpdateSession(type: ReceiveActions, data: ArrayList<Any>)
    }
}