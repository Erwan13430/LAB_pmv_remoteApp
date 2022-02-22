package fr.falling_knife.lab.pmv

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initIHM()
        println("Test")
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
        val toast = Toast.makeText(this, R.string.toastPrepare, Toast.LENGTH_SHORT)
        toast.show()
        val buttons = arrayListOf<Button>(
            findViewById(R.id.btnAVM),
            findViewById(R.id.btnReady),
            findViewById(R.id.btnGo)
        )
        buttons[0].isEnabled=true
        buttons[1].isEnabled=false
        buttons[2].isEnabled=false
    }

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

    private fun getRadioButtons() : ArrayList<RadioButton>{
        val radios = arrayListOf<RadioButton>()

        for(i in 1..20 step 1)
            radios.add(findViewById(resources.getIdentifier("rdb$i", "view", this.packageName)))

        return radios
    }

    fun radioClicked(v: View){
        var id: Int? = null
        val radios = getRadioButtons()
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

        val lineText = findViewById<TextView>(R.id.lineTitleNumber)
        lineText.text = id.toString()
        disableButtons(id!!.toInt())
    }

    private fun disableButtons(id: Int){
        val radioButtons = getRadioButtons()

        for(i in radioButtons){
            if(i != radioButtons[id])
                i.isChecked=false
        }
    }


}

