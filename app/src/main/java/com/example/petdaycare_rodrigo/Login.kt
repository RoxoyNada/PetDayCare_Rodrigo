package com.example.petdaycare_rodrigo

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    lateinit var alertPassTV : TextView
    lateinit var alertMailTV : TextView
    lateinit var mailET : EditText
    lateinit var passET : EditText
    private var lastToast: Toast? = null
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
                        lastToast?.cancel()
                        lastToast=Toast.makeText(this,"Usuario registrado con éxito",Toast.LENGTH_SHORT)
                        lastToast?.show()
                    }else{
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Error Inesperado")
                        builder.setMessage("Se ha producido un error, por favor intentelo de nuevo más tarde")
                        builder.setPositiveButton("Aceptar",null)
                        builder.setIcon(this.getDrawable(R.drawable.error_icon))
                        val dialog = builder.create()
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
                        lastToast?.cancel()
                        lastToast=Toast.makeText(this, "Usuario o Contraseña incorrectos \n por favor revise los campos correspondientes", Toast.LENGTH_LONG)
                        lastToast?.show()
                    }
                }

        }
    }

    private fun isNullBlanckText(text: String):Boolean{
        var noNull = false
        if(text.isBlank() || text.isNullOrEmpty()){
            noNull = true
        }
        return noNull
    }

    fun checkRequiredFields(mail:String, pass:String):Boolean{
        var dataOk = false

        mailET.setHintTextColor(Color.GRAY)
        passET.setHintTextColor(Color.GRAY)
        if (isNullBlanckText(mail) && (!isNullBlanckText(pass))){
            alertMailTV.visibility = View.VISIBLE
            alertPassTV.visibility = View.GONE
            mailET.setHintTextColor(Color.RED)
        }else if ( !isNullBlanckText(mail) && (isNullBlanckText(pass))){
            passET.setHintTextColor(Color.RED)
            mailET.setHintTextColor(Color.GRAY)
            alertMailTV.visibility = View.GONE
            alertPassTV.visibility = View.VISIBLE
        }else if (isNullBlanckText(mail) && (isNullBlanckText(pass))){
            alertMailTV.visibility = View.VISIBLE
            alertPassTV.visibility = View.VISIBLE
            mailET.setHintTextColor(Color.RED)
            passET.setHintTextColor(Color.RED)
        }else{
            passET.setHintTextColor(Color.GRAY)
            mailET.setHintTextColor(Color.GRAY)
            alertMailTV.visibility = View.GONE
            alertPassTV.visibility = View.GONE

            if (!isValidEmail(mail) && isValidPassword(pass)){
                alertPassTV.visibility = View.GONE
                alertMailTV.visibility = View.VISIBLE
                mailET.setTextColor(Color.RED)
                lastToast?.cancel()
                lastToast=Toast.makeText(this, "E-Mail no válido, por favor introduzca un E-mail válido", Toast.LENGTH_LONG)
                lastToast?.show()
            }else if (!isValidPassword(pass) && isValidEmail(mail)){
                alertPassTV.visibility = View.VISIBLE
                alertMailTV.visibility = View.GONE
                mailET.setTextColor(Color.BLACK)
                lastToast?.cancel()
                lastToast=Toast.makeText(this, "Contraseña no válida, por favor introduzca una contraseña válida", Toast.LENGTH_LONG)
                lastToast?.show()
            }else if (!isValidPassword(pass) && !isValidEmail(mail)) {
                alertPassTV.visibility = View.VISIBLE
                alertMailTV.visibility = View.VISIBLE
                mailET.setTextColor(Color.BLACK)
                lastToast?.cancel()
                lastToast = Toast.makeText(
                    this,
                    "E-Mail y contraseña no válidos, por favor introduzca un E-mail y contraseña válidos",
                    Toast.LENGTH_LONG
                )
                lastToast?.show()
            }else{
                alertPassTV.visibility = View.GONE
                alertMailTV.visibility = View.GONE
                mailET.setTextColor(Color.BLACK)
                dataOk = true
            }
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