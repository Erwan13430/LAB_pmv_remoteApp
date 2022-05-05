package fr.falling_knife.lab.pmv
/*
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Process


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var spinnerRunners: ArrayList<Spinner> = ArrayList<Spinner>()
    private var backButtonPressedTime: Long = 0
    private lateinit var backToast: Toast
    //private val dataDial = TcpClient("192.168.0.1", 2314)

    private fun exitApp(){
        finish()
    }

    override fun onDestroy() {
        Process.killProcess(Process.myPid())
        super.onDestroy()
    }

    override fun onBackPressed() {

        if(backButtonPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel()
            moveTaskToBack(false)
            exitApp()
            return
        }else{
            backToast = Toast.makeText(baseContext,"Recliquez pour quitter", Toast.LENGTH_SHORT)
            backToast.show()
        }
        backButtonPressedTime = System.currentTimeMillis()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_main_old)
        initIHM()
        println("Test")

        for(i in 1..40){
            this.spinnerRunners.add(findViewById(resources.getIdentifier("spinRun" + i, "id", packageName)))
        }
        var test: Array<String> = arrayOf("test", "test2", "testé")
        val ar: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, test)

        spinnerRunners.iterator().forEach {
            it.adapter = ar
            it.onItemSelectedListener = this
        }

    }


    fun onBtnSelect(v: View){
        val toast = Toast.makeText(this, R.string.toastSelect, Toast.LENGTH_LONG)
        toast.show()
    }

    fun onBtnCSV(v: View){
        val toast = Toast.makeText(this, R.string.toastCSV, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun onBtnPrepare(v: View){
        val toast = Toast.makeText(this, R.string.toastPrepare, Toast.LENGTH_SHORT) // Création d'une infobulle
        toast.show() // Affichage de l'infobulle de message
        val buttons = arrayListOf<Button>(
            findViewById(R.id.btnAVM),
            findViewById(R.id.btnReady),
            findViewById(R.id.btnGo)
        ) //création d'une liste contenant les différents boutons de gestion de course
        buttons[0].isEnabled=true // Activation du bouton "À vos marques!"
        buttons[1].isEnabled=false // Désactivation du bouton "Prêt !"
        buttons[2].isEnabled=false //Désactivation du bouton "Partez !"
    } // Fonction exécutée lors du clic sur le bouton "Préparation

    fun onBtnAVM(v: View){
        val toast = Toast.makeText(this, R.string.toastAVM, Toast.LENGTH_SHORT)
        toast.show()
        val buttons = arrayListOf<Button>(
            findViewById(R.id.btnReady),
            findViewById(R.id.btnGo)
        )
        buttons[0].isEnabled=true
        buttons[1].isEnabled=false
    }

    fun onBtnReady(v: View){
        val toast = Toast.makeText(this, R.string.toastReady, Toast.LENGTH_SHORT)
        toast.show()
        val button: Button = findViewById(R.id.btnGo)
        button.isEnabled=true
    }

    fun onBtnGo(v : View){
        val buttons = arrayListOf<Button>(
            findViewById(R.id.btnPrep),
            findViewById(R.id.btnAVM),
            findViewById(R.id.btnReady),
            findViewById(R.id.btnGo)
        )
        if(buttons[3].text === getText(R.string.btnGo)){
            val toast = Toast.makeText(this, R.string.toastGo, Toast.LENGTH_SHORT)
            toast.show()
            buttons[0].isEnabled=false
            buttons[1].isEnabled=false
            buttons[2].isEnabled=false
            buttons[3].text=getText(R.string.btnStop)
        }else{
            buttons[0].isEnabled=true
            buttons[1].isEnabled=true
            buttons[2].isEnabled=true
            buttons[3].text=getText(R.string.btnGo)
        }

    }

    private fun initIHM(){
        val buttons = arrayListOf<Button>(
            findViewById(R.id.btnPrep),
            findViewById(R.id.btnAVM),
            findViewById(R.id.btnReady),
            findViewById(R.id.btnGo)
        )

        for(i in buttons){
            i.isEnabled=false
        }

        buttons[0].isEnabled=true
    }

    private fun getSelectButtons() : ArrayList<RadioButton>{
        val radios = arrayListOf<RadioButton>()

        for(i in 1..20 step 1)
            radios.add(findViewById(resources.getIdentifier("rdBtn$i", "id", this.packageName)))

        return radios
    }

    fun radioClicked(v: View){
        var id: Int? = null
        val radios = getSelectButtons()
        println("$v")

        //radios.iterator().forEach { println("$it") }
        when(v){
            radios[0] -> id = 1
            radios[1] -> id = 2
            radios[2] -> id = 3
            radios[3] -> id = 4
            radios[4] -> id = 5
            radios[5] -> id = 6
            radios[6] -> id = 7
            radios[7] -> id = 8
            radios[8] -> id = 9
            radios[9] -> id = 10
            radios[10] -> id = 11
            radios[11] -> id = 12
            radios[12] -> id = 13
            radios[13] -> id = 14
            radios[14] -> id = 15
            radios[15] -> id = 16
            radios[16] -> id = 17
            radios[17] -> id = 18
            radios[18] -> id = 19
            radios[19] -> id = 20
        }

        val lineText = findViewById<TextView>(R.id.lineTitle)
        var text: String = getText(R.string.lineNumber) as String
        lineText.text = text + " " + id?.toString()
        println(id?.toString())
        if (id != null) {
            disableButtons(id.toInt(), radios)
        }
    }

    private fun disableButtons(id: Int, radios: ArrayList<RadioButton>){
        radios.iterator().forEach {
            if(it != radios[id-1]){
                it.isChecked = false
                println("$it disabled")
            }else{
                it.isChecked = true
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val item: String = p0?.getItemAtPosition(p2).toString()
        if(item != "test")
            Toast.makeText(p0?.context, "Selected: " + item, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}


*/