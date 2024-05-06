package com.example.petdaycare_rodrigo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class DataScreen : AppCompatActivity() {

    lateinit var nameET : EditText
    lateinit var ageET : EditText
    lateinit var weigthET : EditText
    lateinit var breedSP : Spinner
    lateinit var genreSP : Spinner
    lateinit var selectedBreed : String
    lateinit var selectedGenre : String
    private var lastToast: Toast? = null
    var breedImgID = 0

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_screen)


        val addPet = findViewById<Button>(R.id.addPetBTN)
        nameET = findViewById(R.id.NameET)
        ageET = findViewById(R.id.AgeET)
        weigthET = findViewById(R.id.WeightET)
        genreSP = findViewById(R.id.GenreSP)
        breedSP = findViewById(R.id.BreedSP)


        val arrayBreed = resources.getStringArray(R.array.breeds_array)
        val adapterBreed = ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayBreed)
        adapterBreed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        breedSP.adapter = adapterBreed

        breedSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                selectedBreed = arrayBreed[pos]
                breedImgID = pos
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val arrayGenre = resources.getStringArray(R.array.genres_array)
        val adapterGenre = ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayGenre)
        adapterGenre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        genreSP.adapter = adapterGenre

        genreSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                selectedGenre = arrayGenre[pos]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }



        addPet.setOnClickListener{

            if(editTextFullfiled(nameET.text.toString(),ageET.text.toString(),weigthET.text.toString())) {
                    var nameSTR = nameET.text.toString()
                    var ageINT = ageET.text.toString().toInt()
                    var weightDouble = weigthET.text.toString().toDouble()

                    if(checkFields(ageINT,weightDouble,selectedBreed,selectedGenre)){
                        createPet(nameSTR,ageINT,selectedBreed,selectedGenre,weightDouble)
                    }else{
                        lastToast?.cancel()
                        lastToast=Toast.makeText(this, "Algun dato es invalido, por favor revise los campos", Toast.LENGTH_SHORT)
                        lastToast?.show()
                    }
            }else{
                lastToast?.cancel()
                lastToast=Toast.makeText(this, "Campos no cumplimentados, por favor rellene todos los campos", Toast.LENGTH_SHORT)
                lastToast?.show()
            }


        }

    }

    fun checkFields(age:Int, weight:Double, breed:String, genre:String): Boolean{
        var dataOk = false

        if((age > 0) && (weight > 0) && breedImgID > 0
            && (!genre.equals("Selecciona una Opción",true))){
            print(breed)
            print(genre)
            dataOk = true
        }else{

            if(genre.equals("Selecciona una Opción",true)){
                lastToast?.cancel()
                lastToast=Toast.makeText(applicationContext, "Selecciona un género", Toast.LENGTH_SHORT)
                lastToast?.show()
                dataOk = false
            }
            if (breedImgID == 0){
                lastToast?.cancel()
                lastToast=Toast.makeText(applicationContext, "Selecciona una raza", Toast.LENGTH_SHORT)
                lastToast?.show()
                dataOk = false
            }
        }
        return dataOk
    }

    fun editTextFullfiled(name: String, age: String, weight: String):Boolean{
        var fullfiled = false
        if ((!nameET.text.isNullOrEmpty() || !nameET.text.isBlank())
            && (!ageET.text.isNullOrEmpty() || !ageET.text.isBlank())
            && (!weigthET.text.isNullOrEmpty() || !weigthET.text.isBlank())){
            fullfiled = true
        }
            return fullfiled
    }



    private fun createPet(name:String, age:Int, breed: String, genre: String, weight: Double){
        val db = Firebase.firestore

        val mascota = hashMapOf(
            "ImagenID" to breedImgID,
            "Nombre" to name,
            "Edad" to age,
            "Raza" to breed,
            "Genero" to genre,
            "Peso" to weight,
        )


        db.collection("Mascotas")
            .add(mascota)
            .addOnSuccessListener { documentReference ->
                lastToast?.cancel()
                lastToast=Toast.makeText(this, "Mascota creada con éxito ", Toast.LENGTH_SHORT)
                lastToast?.show()
            }
            .addOnFailureListener { e ->
                lastToast?.cancel()
                lastToast=Toast.makeText(this, "Ha ocurrido un error $e \n Intentelo más tarde", Toast.LENGTH_SHORT)
                lastToast?.show()

            }
    }
}