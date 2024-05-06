package com.example.petdaycare_rodrigo

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    lateinit var alertPassTV : TextView
    lateinit var alertMailTV : TextView
    lateinit var mailET : EditText
    lateinit var passET : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activivy_login)

        val loginButton = findViewById<Button>(R.id.LoginBTN)
        val registerButton = findViewById<Button>(R.id.RegisterBTN)
        alertPassTV = findViewById(R.id.PassAlertTV)
        alertMailTV = findViewById(R.id.UserAlertTV)
        mailET = findViewById<EditText>(R.id.UserET)
        passET = findViewById<EditText>(R.id.PassET)

        loginButton.setOnClickListener{
            loginUser()
        }

        registerButton.setOnClickListener{
           registerNewUser()
        }
    }


    fun registerNewUser(){

        val mailSTR = mailET.text.toString()
        val passSTR = passET.text.toString()

        if(checkRequiredFields(mailSTR,passSTR)){
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(mailSTR,passSTR).addOnCompleteListener{

                    if(it.isSuccessful){
                        Toast.makeText(this,"Usuario registrado con éxito",Toast.LENGTH_SHORT).show()
                    }else{
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("Error Inesperado")
                        builder.setMessage("Se ha producido un error, por favor intentelo de nuevo más tarde")
                        builder.setPositiveButton("Aceptar",null)
                        builder.setIcon(this.getDrawable(R.drawable.error_icon))
                        var dialog = builder.create()
                        dialog.show()
                    }
                }

        }
    }


    fun loginUser(){

        val mailSTR = mailET.text.toString()
        val passSTR = passET.text.toString()

        if(checkRequiredFields(mailSTR,passSTR)){
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(mailSTR,passSTR).addOnCompleteListener{
                    if (it.isSuccessful) {
                        val i = Intent(applicationContext, MainActivity::class.java)
                        startActivity(i)
                    } else {
                        Toast.makeText(this, "Usuario o Contraseña incorrectos \n por favor revise los campos correspondientes", Toast.LENGTH_LONG)
                            .show()
                    }
                }

        }
    }





    fun checkRequiredFields(mail:String, pass:String):Boolean{
        var dataOk = false

        mailET.setHintTextColor(Color.GRAY)
        passET.setHintTextColor(Color.GRAY)
        if ((mail.isNullOrEmpty() || mail.isBlank()) && (!pass.isNullOrEmpty() || !pass.isBlank())){
            Toast.makeText(this,"Cumplimente el campo de E-mail correctamente",Toast.LENGTH_SHORT).show()
            alertMailTV.visibility = View.VISIBLE
            mailET.setHintTextColor(Color.RED)
        }else if ((pass.isNullOrEmpty() || pass.isBlank()) && (!mail.isNullOrEmpty() || !mail.isBlank())){
            Toast.makeText(this,"Cumplimente el campo de contraseña correctamente",Toast.LENGTH_SHORT).show()
            passET.setHintTextColor(Color.RED)
            alertPassTV.visibility = View.VISIBLE
        }else if ((mail.isNullOrEmpty() || mail.isBlank()) && (pass.isNullOrEmpty() || pass.isBlank())){
            Toast.makeText(this,"Cumplimente los campos de E-mail y contraseña correctamente",Toast.LENGTH_SHORT).show()
            alertPassTV.visibility = View.VISIBLE
            alertMailTV.visibility = View.VISIBLE
            mailET.setHintTextColor(Color.RED)
            passET.setHintTextColor(Color.RED)
        }else if (!isValidEmail(mail)){
            Toast.makeText(this,"Cumplimente el campo de E-mail con un correo electrónico válido ",Toast.LENGTH_SHORT).show()
            alertMailTV.visibility = View.VISIBLE
            mailET.setTextColor(Color.RED)
        }else if (!isValidPassword(pass)){
            Toast.makeText(this,"Cumplimente el contraseña con una contraseña válida ",Toast.LENGTH_SHORT).show()
            alertPassTV.visibility = View.VISIBLE
        } else{
            dataOk = true
            alertPassTV.visibility = View.GONE
            alertMailTV.visibility = View.GONE
            mailET.setTextColor(Color.BLACK)
        }
        return dataOk
    }

    fun isValidEmail(mail: String): Boolean{
        val mailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        return mail.matches(mailRegex)
    }


    // (?=.*[a-z]): Contiene al menos una letra minúscula.
    //(?=.*[A-Z]): Contiene al menos una letra mayúscula.
    //(?=.*\d): Contiene al menos un dígito (número).
    //.{6,}: Tiene al menos 6 caracteres en total.

    fun isValidPassword(pass: String): Boolean{
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$")
        return pass.matches(passwordRegex)
    }


}