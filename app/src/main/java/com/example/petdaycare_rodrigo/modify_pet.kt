package com.example.petdaycare_rodrigo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class modify_pet : AppCompatActivity() {
    lateinit var petId: String
    lateinit var deleteBTN: Button
    private var lastToast: Toast? = null

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_pet)
        val pet = intent.getParcelableExtra<Mascota>("Mascota")
        deleteBTN = findViewById(R.id.DeletePetBTN)

        if (pet != null) {
            petId = pet.id
        }

        deleteBTN.setOnClickListener{
            deletePet()
        }


    }


    private fun deletePet() {
        var db = Firebase.firestore

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alerta")
        builder.setMessage("Está apunto de borrar una mascota de la base de datos \n ¿Estás seguro de que deseas continuar?")

        builder.setPositiveButton("Aceptar") { dialog, wich ->
            db.collection("Mascotas").document(petId)
                .delete()
                .addOnSuccessListener {
                    lastToast?.cancel()
                    lastToast=Toast.makeText(this, "Mascota borrada satisfactoriamente", Toast.LENGTH_SHORT)
                    lastToast?.show()
                }
                .addOnFailureListener { e ->
                    lastToast?.cancel()
                    lastToast=Toast.makeText(this, "Ha ocurrido un error inesperado: $e , por favor intentelo más tarde", Toast.LENGTH_SHORT)
                    lastToast?.show()
                }
        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }
}