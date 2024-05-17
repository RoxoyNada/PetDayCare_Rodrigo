package com.example.petdaycare_rodrigo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.ColorSpace.Rgb
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class AdapterMascotas(context: Context, viewToPaint: Int, private val mascotas: ArrayList<Mascota>):
    ArrayAdapter<Mascota>(context,viewToPaint,mascotas) {

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val currentListItem = inflater.inflate(R.layout.activity_adapter_list_view, null)
        val iconoIv = currentListItem.findViewById<ImageView>(R.id.PetIconIV)
        val nombreMascotaTV = currentListItem.findViewById<TextView>(R.id.GetNameTV2)
        val razaMascotaTV = currentListItem.findViewById<TextView>(R.id.GetBreedTV2)
        val weight = currentListItem.findViewById<TextView>(R.id.WeightTV2)
        val weightStr = mascotas.get(position).peso.toString()

        iconoIv.setImageResource(mascotas.get(position).imagen)
        nombreMascotaTV.text = mascotas.get(position).nombre
        razaMascotaTV.text = mascotas.get(position).raza
        weight.text="$weightStr Kg"

        if(mascotas.get(position).genero =="Hembra"){
            iconoIv.setBackgroundColor(Color.rgb(250,200,230))
        }

        return currentListItem
    }

}