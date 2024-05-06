package com.example.petdaycare_rodrigo

import android.os.Parcel
import android.os.Parcelable

data class Mascota(var id: String, var imagen: Int, var nombre: String, var edad: Int, var raza: String, var genero: String, var peso: Double):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readInt(),
        parcel.readString()?:"",
        parcel.readInt(),
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(imagen)
        parcel.writeString(nombre)
        parcel.writeInt(edad)
        parcel.writeString(raza)
        parcel.writeString(genero)
        parcel.writeDouble(peso)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mascota> {
        override fun createFromParcel(parcel: Parcel): Mascota {
            return Mascota(parcel)
        }

        override fun newArray(size: Int): Array<Mascota?> {
            return arrayOfNulls(size)
        }
    }

}
