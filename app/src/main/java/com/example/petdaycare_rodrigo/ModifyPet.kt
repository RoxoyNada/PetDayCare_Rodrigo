package com.example.petdaycare_rodrigo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
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

class ModifyPet : AppCompatActivity() {
    lateinit var petId: String
    lateinit var deleteBTN: Button
    lateinit var modifyBTN: Button
    lateinit var nameET : EditText
    lateinit var ageET : EditText
    lateinit var weigthET : EditText
    lateinit var breedSP : Spinner
    lateinit var genreSP : Spinner
    lateinit var selectedBreed : String
    lateinit var selectedGenre : String
    lateinit var breedImg: ShapeableImageView
    var breedImgID = 0
    private var lastToast: Toast? = null

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_pet)
        val pet = intent.getParcelableExtra<Mascota>("Mascota")

        deleteBTN = findViewById(R.id.DeletePetBTN)
        modifyBTN = findViewById(R.id.ModifyPetBTN)
        nameET = findViewById(R.id.NameET2)
        ageET = findViewById(R.id.AgeET2)
        weigthET = findViewById(R.id.WeightET2)
        genreSP = findViewById(R.id.GenreSP2)
        breedSP = findViewById(R.id.BreedSP2)
        breedImg = findViewById(R.id.BreedIV)

        val arrayBreed = resources.getStringArray(R.array.breeds_array)
        val adapterBreed = ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayBreed)
        adapterBreed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        breedSP.adapter = adapterBreed

        breedSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                selectedBreed = arrayBreed[pos]
                breedImgID = pos
                drawPet(pos)
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

        if (pet != null) {
            petId = pet.id
        }

        pet?.let {
            nameET.setText(it.nombre)
            weigthET.setText(it.peso.toString())
            ageET.setText(it.edad.toString())

            val genre = resources.getStringArray(R.array.genres_array)
            val indexGenre = genre.indexOf(it.genero)
            if (indexGenre != -1) {
                genreSP.setSelection(indexGenre)
            }

            val breed = resources.getStringArray(R.array.breeds_array)
            val indexBreed = breed.indexOf(it.raza)
            if (indexBreed != -1 || indexBreed > 0) {
                breedSP.setSelection(indexBreed)
                drawPet(indexBreed)
            }

        }


        deleteBTN.setOnClickListener{
            deletePet()
        }

        modifyBTN.setOnClickListener{
            if (selectedBreed.equals("Selecciona una Opción",true) && !selectedGenre.equals("Selecciona una Opción",true) ){
                lastToast?.cancel()
                lastToast=Toast.makeText(this, "Selecciona una raza", Toast.LENGTH_SHORT)
                lastToast?.show()
            }else if(selectedGenre.equals("Selecciona una Opción",true) && !selectedBreed.equals("Selecciona una Opción",true)){
                lastToast?.cancel()
                lastToast=Toast.makeText(this, "Selecciona un género", Toast.LENGTH_SHORT)
                lastToast?.show()
            } else if(selectedGenre.equals("Selecciona una Opción",true) && selectedBreed.equals("Selecciona una Opción",true) ){
                lastToast?.cancel()
                lastToast=Toast.makeText(this, "Selecciona una raza y un género", Toast.LENGTH_SHORT)
                lastToast?.show()
            }else if(!editTextFullfiled()){
                lastToast?.cancel()
                lastToast=Toast.makeText(this, "Cumplimente todos los campos ", Toast.LENGTH_SHORT)
                lastToast?.show()
            }else{
                if(weigthET.text.toString()=="."){
                    weigthET.setText("0")
                }
                modifyPet(selectedBreed, selectedGenre, breedImgID)
            }
        }

    }
    fun drawPet(pos:Int){
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
        breedImg.setImageResource(drawableId)

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
            lastToast=Toast.makeText(this, "Cumplimente todos los datos", Toast.LENGTH_SHORT)
            lastToast?.show()
        }
        return fullfiled
    }

    private fun deletePet() {
        val db = Firebase.firestore

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
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
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

    private fun modifyPet(breed : String, genre : String, imgValue:Int) {
        val db = Firebase.firestore

        val name = nameET.text.toString().trim()
        val weigth = weigthET.text.toString().toDouble()
        val age = ageET.text.toString().toInt()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alerta")
        builder.setMessage("¿Seguro que quieres modificar la mascota?")
        builder.setPositiveButton("Sí, seguro") { dialog, wich ->
            db.collection("Mascotas").document(petId)
                .update(
                    mapOf(
                        "Edad" to age,
                        "Genero" to genre,
                        "ImagenID" to imgValue,
                        "Nombre" to name,
                        "Peso" to weigth,
                        "Raza" to breed,
                    ),
                )
                .addOnSuccessListener   {
                    Toast.makeText(this, "La mascota se ha actualizado con éxito.", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "No se ha podido actualizar la mascota, inténtelo más tarde.", Toast.LENGTH_SHORT).show()
                }
        }
        builder.setNegativeButton("No, cancelar") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }
}