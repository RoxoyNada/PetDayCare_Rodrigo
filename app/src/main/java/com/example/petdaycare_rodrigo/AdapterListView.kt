package com.example.petdaycare_rodrigo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class AdapterListView(context: Context, viewToPaint: Int, private val mascotas: ArrayList<Mascota>):
    ArrayAdapter<Mascota>(context,viewToPaint,mascotas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val currentListItem = inflater.inflate(R.layout.activity_adapter_list_view, null)

        val iconoIv = currentListItem.findViewById<ImageView>(R.id.PetIconIV_OLD)
        val nombreMascotaTV = currentListItem.findViewById<TextView>(R.id.GetNameTV_OLD)
        val razaMascotaTV = currentListItem.findViewById<TextView>(R.id.GetBreedTV_OLD)

        iconoIv.setImageResource(mascotas.get(position).imagen)
        nombreMascotaTV.text = mascotas.get(position).nombre
        razaMascotaTV.text = mascotas.get(position).raza

        return currentListItem
    }

}