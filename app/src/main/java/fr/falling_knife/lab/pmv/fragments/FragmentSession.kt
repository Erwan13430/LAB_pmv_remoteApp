package fr.falling_knife.lab.pmv.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

    private var _login: String? = null
    private var _password: String? = null
    private var _serverAddress: String? = null
    private var _serverPort: String? = null
    private lateinit var _listener: OnSessionManagement
    private lateinit var _rootView: View

    /* Initialisation du fragment */
    
    override fun onAttach(context: Context) {

        super.onAttach(context)

        _listener = if(context is OnSessionManagement) context else let {
            throw ClassCastException("$context must implement params")
        }

    } // onAttach

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        arguments?.let {
            _login = it.getString(ARG_PARAM1)
            _password = it.getString(ARG_PARAM2)
            _serverAddress = it.getString(ARG_PARAM3)
            _serverPort = it.getString(ARG_PARAM4)
        }

    } // onCreate

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /* Récupération des layouts du fragment */
        _rootView = inflater.inflate(R.layout.fragment_session, container, false)

        if(savedInstanceState == null){

            /* Récupération des boutons de gestion de course */
            val btnGest: ArrayList<Button> = getGestBtns()
            val btnStopSess: Button = btnGest[0]
            val btnPrep: Button = btnGest[1]
            val btnAvm: Button = btnGest[2]
            val btnReady: Button = btnGest[3]
            val btnGo: Button = btnGest[4]
            val btnControl: Button = btnGest[5]


            /* Initialisation des boutons de gestion */
            btnStopSess.setOnClickListener { btnSelectListener() }
            btnPrep.setOnClickListener{ btnPreparationListener(btnGest) }
            btnAvm.setOnClickListener{ btnAVMListener(btnGest) }
            btnReady.setOnClickListener{ btnReadyListener(btnGest) }
            btnGo.setOnClickListener { btnGoListener(btnGest) }
            btnControl.setOnClickListener { btnControlListener() }
            btnControl.isVisible = false

            /* Récupération et initialisation des boutons radio */
            val rdBtns: ArrayList<RadioButton> = getSelectButton()
            initRadioButtons(rdBtns)

            /* Initialisation affichage initial de l'IHM */
            initIHM(btnGest, rdBtns)

        } //if

        /* Démarrage de la session de course */
        Log.d("FrgSess::onCreateView", "Starting onSessionRunning")
        _listener.onSessionRunning(DataApp(_login!!, _password!!, _serverAddress!!, _serverPort!!.toInt(), "onSessionRunning"))

        return _rootView

    } // onCreateView


    /* ----------------------------------------- */
    /* Fonctions d'initialisation de l'interface */
    /* ----------------------------------------- */
    private fun initIHM(buttons: ArrayList<Button>, rdBtns: ArrayList<RadioButton>) {

        for(i in 0 until buttons.count()){
            buttons[i].isEnabled = i == 0 || i == 1
        }

        rdBtns[0].isChecked = true
        val text = String.format(resources.getString(R.string.lineNumber), "1")
        _rootView.findViewById<TextView>(R.id.lineTitle).text = text
        _rootView.findViewById<TextView>(R.id.txtSessionName).text = String.format(resources.getString(R.string.sessionName), resources.getString(R.string.defaultSessionName))

    } // initIHM

    private fun initSpinners(spinners: ArrayList<Spinner>, adapter: ArrayAdapter<String>) {

        spinners.iterator().forEach{
            it.adapter = adapter
            it.isEnabled = false
        }
        spinners[0].isEnabled = true
        spinners[1].isEnabled = true

    } //initSpinners

    private fun initRadioButtons(rdBtns: ArrayList<RadioButton>) {

        for(i in 0 until rdBtns.count()){

            rdBtns[i].setOnClickListener{
                rdButtonListener(rdBtns, it)
            }

            rdBtns[i].isEnabled = i == 0

        }

    } // initRadioButtons

    /* --------------------------------------------- */
    /* Fonctions de récupération des listes d'objets */
    /* --------------------------------------------- */
    private fun getSelectButton(): ArrayList<RadioButton> {

        val radios = arrayListOf<RadioButton>()

        for(i in 1..20 step 1)
            radios.add(_rootView.findViewById(resources.getIdentifier("rdBtn$i", "id", activity?.packageName)))

        return radios

    } // getSelectButton

    private fun getSpinners(): ArrayList<Spinner> {

        val spinner = arrayListOf<Spinner>()

        for(i in 1..40 step 1)
            spinner.add(_rootView.findViewById(resources.getIdentifier("spinRun$i", "id", activity?.packageName)))

        return spinner

    } // getSpinners

    private fun getGestBtns(): ArrayList<Button> {

        return arrayListOf(
            _rootView.findViewById(R.id.btnSelect),
            _rootView.findViewById(R.id.btnPrep),
            _rootView.findViewById(R.id.btnAVM),
            _rootView.findViewById(R.id.btnReady),
            _rootView.findViewById(R.id.btnGo),
            _rootView.findViewById(R.id.btnControl)
        )

    } // getGestBtns

    /* ------------------------------ */
    /* Fonctions listeners des objets */
    /* ------------------------------ */
    private fun btnSelectListener() {

        Toast.makeText(activity?.baseContext, resources.getString(R.string.endSession), Toast.LENGTH_SHORT).show()

        disableInterface()

        _listener.onSendCommand(DataSend(SendAction.BUTTON, arrayListOf("1", "0", "0", "0", "0", "0")))
    } // btnSelectListener

    private fun btnControlListener() {

        enableInterfaceGC()

    } // btnControlListener

    private fun btnPreparationListener(btnGest: ArrayList<Button>) {

        Toast.makeText(activity?.baseContext, "Préparation", Toast.LENGTH_SHORT).show()

        for(i in 0 until btnGest.count())
            btnGest[i].isEnabled = i == 0 || i == 2

        _listener.onSendCommand(DataSend(SendAction.BUTTON, arrayListOf("0", "1", "0", "0", "0", "0")))

    } // btnPreparationListener

    private fun btnAVMListener(btnGest: ArrayList<Button>) {

        Toast.makeText(activity?.baseContext, "À vos marques", Toast.LENGTH_SHORT).show()

        for(i in 0 until btnGest.count())
            btnGest[i].isEnabled = i == 0 || i == 1 || i == 3

        _listener.onSendCommand(DataSend(SendAction.BUTTON, arrayListOf("0", "0", "1", "0", "0", "0")))

    } // btnAVMListener

    private fun btnReadyListener(btnGest: ArrayList<Button>) {

        Toast.makeText(activity?.baseContext, "Prêt", Toast.LENGTH_SHORT).show()

        for(i in 0 until btnGest.count())
            btnGest[i].isEnabled = i == 0 || i == 2 || i == 4

        _listener.onSendCommand(DataSend(SendAction.BUTTON, arrayListOf("0", "0", "0", "1", "0", "0")))

    } // btnReadyListener

    private fun btnGoListener(btnGest: ArrayList<Button>) {

        if(btnGest[4].text == resources.getText(R.string.btnGo)) {

            for(i in 0 until btnGest.count())
                btnGest[i].isEnabled = i == 4

            btnGest[4].text = resources.getText(R.string.btnStop)
            Toast.makeText(activity?.baseContext, "Partez !", Toast.LENGTH_SHORT).show()


            _listener.onSendCommand(DataSend(SendAction.BUTTON, arrayListOf("0", "0", "0", "0", "1", "0")))

            val rdBtns = getSelectButton()
            val spinners = getSpinners()
            var id: Int = 0

            for(i in 0 until rdBtns.count())
                id = if(rdBtns[i].isChecked) i else id



        }else{

            for(i in 0 until btnGest.count())
                btnGest[i].isEnabled = i == 0 || i == 1

            btnGest[4].text = resources.getText(R.string.btnGo)
            Toast.makeText(activity?.baseContext, "Arrêt de la course !", Toast.LENGTH_SHORT).show()


            _listener.onSendCommand(DataSend(SendAction.BUTTON, arrayListOf("0", "0", "0", "0", "0", "1")))

        }

    } // btnGoListener

    private fun rdButtonListener(rdBtns: ArrayList<RadioButton>, radio: View) {

        var id: Int = 0

        for(i in 0 until rdBtns.count())
            id = if (id == 0)
                if (radio == rdBtns[i]) i + 1 else 0
            else
                id

        val lineText = activity?.findViewById<TextView>(R.id.lineTitle)
        val text: String = String.format(resources.getString(R.string.lineNumber), id)
        lineText?.text = text
        Log.d("FrgSess::ID", id.toString())
        disableButtons(id, rdBtns)

    } // rbButtonListener

    /* ---------------------------------------- */
    /* Fonctions de modification de l'interface */
    /* ---------------------------------------- */
    private fun disableButtons(id: Int, radios: ArrayList<RadioButton>) {

        radios.iterator().forEach {

            it.isChecked = it == radios[id-1]
            Log.d("FrgSess::Btn", "$it is ${it.isChecked}")

        }

    } // disableButton

    fun disableInterfaceGC() {

        disableInterface()

        stateControlButton(true)

    } // disableInterfaceGC

    private fun disableInterface(){

        getSelectButton().iterator().forEach {

            it.isEnabled = false

        }

        getSpinners().iterator().forEach {

            it.isEnabled = false

        }

        _rootView.findViewById<Button>(R.id.btnSelect).isEnabled = false
        _rootView.findViewById<Button>(R.id.btnPrep).isEnabled = false
        _rootView.findViewById<Button>(R.id.btnAVM).isEnabled = false
        _rootView.findViewById<Button>(R.id.btnReady).isEnabled = false
        _rootView.findViewById<Button>(R.id.btnGo).isEnabled = false

        val color =
            if(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
                R.color.dark_teal
            else
                R.color.light_teal

        _rootView.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarSelect).setBackgroundColor(ContextCompat.getColor(requireActivity(), color))
        _rootView.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarControl).setBackgroundColor(ContextCompat.getColor(requireActivity(), color))
        activity?.window?.statusBarColor = ContextCompat.getColor(requireActivity(), color)

    } // disableInterface

    private fun enableInterfaceGC() {

        enableInterface()

        stateControlButton(false)

        _listener.onSendCommand(DataSend(SendAction.CONTROL, arrayListOf()))

    } // enableInterfaceGC

    private fun enableInterface(){

        getSelectButton().iterator().forEach {

            it.isEnabled = true

        }

        getSpinners().iterator().forEach {

            it.isEnabled = true

        }

        _rootView.findViewById<Button>(R.id.btnSelect).isEnabled = true
        _rootView.findViewById<Button>(R.id.btnPrep).isEnabled = true
        _rootView.findViewById<Button>(R.id.btnAVM).isEnabled = true
        _rootView.findViewById<Button>(R.id.btnReady).isEnabled = true
        _rootView.findViewById<Button>(R.id.btnGo).isEnabled = true

        val color =
            if(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
                R.color.dark_red
            else
                R.color.light_red

        _rootView.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarSelect).setBackgroundColor(ContextCompat.getColor(requireActivity(), color))
        _rootView.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarControl).setBackgroundColor(ContextCompat.getColor(requireActivity(), color))
        activity?.window?.statusBarColor = ContextCompat.getColor(requireActivity(), color)

    } // enableInterface

    private fun stateControlButton(state: Boolean) {

        val ctrlButton: Button = _rootView.findViewById(R.id.btnControl)

        ctrlButton.isVisible = state
        ctrlButton.isEnabled = state

    } // stateControlButton

    fun setRunnersList(runners: ArrayList<Any>) {

        val runnersList: ArrayList<String> = runners as ArrayList<String>
        _rootView.findViewById<TextView>(R.id.txtSessionName).text = String.format(resources.getString(R.string.sessionName), runnersList[0])
        Log.d("FrgSess::setRunnerList", "Session Name: ${runnersList[0]}")
        runnersList.removeAt(0)
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(_rootView.context, android.R.layout.simple_spinner_dropdown_item, runnersList)
        initSpinners(getSpinners(), arrayAdapter)

    } // setRunnersList

    fun restoreSession(data: ArrayList<Any>) {

        val session: ArrayList<ArrayList<String>> = data as ArrayList<ArrayList<String>>

        for(i in 1..session.count()) {

            val id: Int = session[i - 1][0].toInt()
            var spinner = _rootView.findViewById<Spinner>(resources.getIdentifier("spinRun${(i * 2) - 1}", "id", activity?.packageName))
            var adapt = spinner.adapter as ArrayAdapter<String>

            spinner.setSelection(adapt.getPosition(session[i - 1][1]))
            _rootView.findViewById<TextView>(resources.getIdentifier("txtTime${(id * 2) - 1}", "id", activity?.packageName)).text = session[i - 1][2]
            _rootView.findViewById<TextView>(resources.getIdentifier("txtWind${(id * 2) - 1}", "id", activity?.packageName)).text = session[i - 1][3]
            _rootView.findViewById<TextView>(resources.getIdentifier("txtSpeed${(id * 2) - 1}", "id", activity?.packageName)).text = session[i - 1][4]


            spinner = _rootView.findViewById(resources.getIdentifier("spinRun${id * 2}", "id", activity?.packageName))
            adapt = spinner.adapter as ArrayAdapter<String>
            spinner.setSelection(adapt.getPosition(session[i - 1][5]))
            _rootView.findViewById<TextView>(resources.getIdentifier("txtTime${id * 2}", "id", activity?.packageName)).text = session[i - 1][6]
            _rootView.findViewById<TextView>(resources.getIdentifier("txtWind${id * 2}", "id", activity?.packageName)).text = session[i - 1][7]
            _rootView.findViewById<TextView>(resources.getIdentifier("txtSpeed${id * 2}", "id", activity?.packageName)).text = session[i - 1][8]

        }
        val rdBtns: ArrayList<RadioButton> = getSelectButton()
        val spinners: ArrayList<Spinner> = getSpinners()

        for(i in 0 until rdBtns.count()){
            rdBtns[i].isEnabled = (i + 1) == session[session.count() - 1][0].toInt() || (i + 1) == (session[session.count() -1][0].toInt() + 1)
        }

        rdBtns[session.count() - 1].isChecked = true

        for(i in 0 until spinners.count()){
            spinners[i].isEnabled = (i + 1) == (((session[session.count() - 1][0].toInt() + 1) * 2)) || (i + 1) == ((session[session.count() - 1][0].toInt() + 1) * 2 - 1)
        }


    } // restoreSession

    fun controlButtons(data: ArrayList<Any>) {

        val btnGest = getGestBtns()
        val states = data as ArrayList<Int>

        val index: Int = states.indexOf(1)

        for(i in 0 until states.count())
            btnGest[i].isEnabled = (i == index - 1) or (i == index + 1)

        btnGest[0].isEnabled = true

    } // controlButtons

    fun endSession(){
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(resources.getString(R.string.alrtbox_endsess_title))
        alertDialog.setMessage(resources.getString(R.string.alrtbox_endsess_msg))
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
        DialogInterface.OnClickListener() { dialog, which ->
            fun onClick(dialog: DialogInterface, which: Int){
                dialog.dismiss()
                activity?.finishActivity(0)
            }
        });
        alertDialog.show()
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
        fun onEndSession()
        fun onUpdateSession(type: ReceiveActions, data: ArrayList<Any>)
    } // OnSessionManagement
}