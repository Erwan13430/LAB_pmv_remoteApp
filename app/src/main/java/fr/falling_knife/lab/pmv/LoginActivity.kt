package fr.falling_knife.lab.pmv

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

const val EXTRA_MESSAGE = "fr.falling_knife.lab.pmv.MESSAGE"

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    fun onBtnLogin(view: View){
        val info = Toast.makeText(this, "Vérification des identifiants...", Toast.LENGTH_LONG)
        info.show()

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, "Coucou")
        }

        startActivity(intent)
    }


}