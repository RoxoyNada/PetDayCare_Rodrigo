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
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CreatePet : AppCompatActivity() {

    lateinit var nameET : EditText
    lateinit var ageET : EditText
    lateinit var weigthET : EditText
    lateinit var breedSP : Spinner
    lateinit var genreSP : Spinner
    lateinit var selectedBreed : String
    lateinit var selectedGenre : String
    lateinit var dogImage: ShapeableImageView
    private var lastToast: Toast? = null
    var breedImgID = 0

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pet_screen)


        val addPet = findViewById<Button>(R.id.addPetBTN)
        nameET = findViewById(R.id.NameET)
        ageET = findViewById(R.id.AgeET)
        weigthET = findViewById(R.id.WeightET)
        genreSP = findViewById(R.id.GenreSP)
        breedSP = findViewById(R.id.BreedSP)
        dogImage = findViewById(R.id.DogImage)


        val arrayBreed = resources.getStringArray(R.array.breeds_array)
        val adapterBreed = ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayBreed)
        adapterBreed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        breedSP.adapter = adapterBreed

        breedSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                selectedBreed = arrayBreed[pos]
                breedImgID = pos
                val drawableId = when (pos) {
                    1 -> R.drawable.beagle
                    2 -> R.drawable.bedlington
                    3 -> R.drawable.bichon
                    4 -> R.drawable.bodeguero
                    5 -> R.drawable.border
                    6 -> R.drawable.borzoi
                    7 -> R.drawable.bull_frances
                    8 -> R.drawable.bull_ingles
                    9 -> R.drawable.chihuahua
                    10 -> R.drawable.chow
                    11 -> R.drawable.corgi
                    12 -> R.drawable.galgo
                    13 -> R.drawable.golden
                    14 -> R.drawable.danes
                    15 -> R.drawable.husky
                    16 -> R.drawable.mastin
                    17 -> R.drawable.cane_corso
                    18 -> R.drawable.aleman
                    19 -> R.drawable.aguas
                    20 -> R.drawable.salchicha
                    21 -> R.drawable.pinscher
                    22 -> R.drawable.pitbull
                    23 -> R.drawable.pomeranian
                    24 -> R.drawable.rotweiller
                    25 -> R.drawable.sabueso_espannol
                    26 -> R.drawable.san_bernardo
                    27 -> R.drawable.shiba
                    28 -> R.drawable.west_terrier
                    29 -> R.drawable.yorkshire
                    30 -> R.drawable.default_pet_icon
                    0 -> R.drawable.default_pet_icon
                    else -> throw IllegalArgumentException("Número fuera de rango")
                }

                dogImage.setImageResource(drawableId)
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

            if(editTextFullfiled()) {
                if(weigthET.text.toString()=="."){
                    weigthET.setText("0")
                }
                    val nameSTR = nameET.text.toString()
                    val ageINT = ageET.text.toString().toInt()
                    val weightDouble = weigthET.text.toString().toDouble()
                    if(checkFields(ageINT,weightDouble,selectedGenre)){
                        createPet(nameSTR,ageINT,selectedBreed,selectedGenre,weightDouble)
                    }else{
                        if (weigthET.text.toString().toDouble()>=100){
                            lastToast?.cancel()
                            lastToast=Toast.makeText(this, "Peso máximo 99.9Kg ", Toast.LENGTH_SHORT)
                            lastToast?.show()
                        }else{
                            lastToast?.cancel()
                            lastToast=Toast.makeText(this, "Por favor seleccione raza y género", Toast.LENGTH_SHORT)
                            lastToast?.show()
                        }
                    }
            }
        }
    }

    fun checkFields(age:Int, weight:Double, genre:String): Boolean{
        var dataOk = false

        if((age > 0) && (weight > 0 && weight < 100) && breedImgID > 0
            && (!genre.equals("Selecciona una Opción",true))){
            dataOk = true
        }
        return dataOk
    }

    fun editTextFullfiled():Boolean{
        var fullfiled = false
        val nameText = nameET.text.toString().trim()
        if ((nameText.isNotEmpty() && !nameText.isBlank())
            && (!ageET.text.isNullOrEmpty())
            && (!weigthET.text.isNullOrEmpty())){
            fullfiled = true
        }
        else{
            lastToast?.cancel()
            lastToast=Toast.makeText(this, "Campos no cumplimentados, por favor rellene todos los campos", Toast.LENGTH_SHORT)
            lastToast?.show()
        }
            return fullfiled
    }

    private fun createPet(name:String, age:Int, breed: String, genre: String, weight: Double){
        val db = Firebase.firestore

        val mascota = hashMapOf(
            "ImagenID" to breedImgID,
            "Nombre" to name.trim(),
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