package com.example.petdaycare_rodrigo

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AdapterMascotas(context: Context, viewToPaint: Int, private val mascotas: ArrayList<Mascota>):
    ArrayAdapter<Mascota>(context,viewToPaint,mascotas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val currentListItem = inflater.inflate(R.layout.activity_adapter_list_view, null)

        val iconoIv = currentListItem.findViewById<ImageView>(R.id.PetIconIV_OLD)
        val nombreMascotaTV = currentListItem.findViewById<TextView>(R.id.GetNameTV_OLD)
        val razaMascotaTV = currentListItem.findViewById<TextView>(R.id.GetBreedTV_OLD)

//        val trashIV = currentListItem.findViewById<ImageButton>(R.id.TrashIconIV_OLD)


        iconoIv.setImageResource(mascotas.get(position).imagen)
        nombreMascotaTV.text = mascotas.get(position).nombre
        razaMascotaTV.text = mascotas.get(position).raza

//        trashIV.setOnClickListener {
//            deletePet()
//        }

        if (isEven(position) == 0) {
            currentListItem.setBackgroundColor(Color.rgb(225, 230, 255))
        } else {
            currentListItem.setBackgroundColor(Color.rgb(255, 235, 205))
        }

        return currentListItem
    }

    fun isEven(position: Int): Int {
        var res = position % 2
        return res
    }

/* PARTE1: PREGUNTAR BRAIS
    fun deletePet() {
        var db = Firebase.firestore

        db.collection("Mascotas").document("DC")
            .delete()
            .addOnSuccessListener { Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Toast.makeText(context, "Ha ocurrido un error inesperado: $e , por favor intentelo más tarde", Toast.LENGTH_SHORT).show()}
*/

/*        val builder = AlertDialog.Builder(context.applicationContext)
        builder.setTitle("Alerta")
        builder.setMessage("Está apunto de borrar una mascota de la base de datos \n ¿Estás seguro de que deseas continuar?")

        builder.setPositiveButton("Aceptar"){ dialog, wich ->
        db.collection("Mascotas").document("DC")
            .delete()
            .addOnSuccessListener {
                    val builder = AlertDialog.Builder()
                    builder.setTitle("Datos Borrados")
                    builder.setMessage("Mascota borrada")
                    builder.setPositiveButton("Aceptar"){ dialog, wich ->
                    }
                    builder.show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Ha ocurrido un error inesperado: $e , por favor intentelo más tarde", Toast.LENGTH_SHORT).show()
                }
        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()*/
            }
//}


