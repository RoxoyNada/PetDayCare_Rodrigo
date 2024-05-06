package com.example.petdaycare_rodrigo

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    var petsArray = arrayListOf<Mascota>()

    override fun onResume() {
        super.onResume()
        getPetCollection()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var floatBTN = findViewById<FloatingActionButton>(R.id.AddPetBTN)
        floatBTN.setOnClickListener{
            val i = Intent(applicationContext, DataScreen::class.java)
            startActivity(i)
            petsArray.clear()
        }
    }

    fun getPetCollection(){
        val db = Firebase.firestore
        db.collection("Mascotas")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val petId = document.id
                    val currentAge = document.data["Edad"].toString().toInt()
                    val currentGenre = document.data["Genero"].toString()
                    val currentImgId = document.data["ImagenID"].toString().toInt()
                    val currentName = document.data["Nombre"].toString()
                    val currentWeight = document.data["Peso"].toString().toDouble()
                    val currentBreed = document.data["Raza"].toString()

                    val drawableId = when (currentImgId) {
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

                    val currentPet = Mascota(petId,drawableId,currentName,currentAge,currentBreed,currentGenre,currentWeight)
                    petsArray.add(currentPet)
                }

                val emptyTV = findViewById<TextView>(R.id.EmptyViewTV)
                val listPetsView = findViewById<ListView>(R.id.PetsLV)
                listPetsView.emptyView = emptyTV


                var petList = AdapterMascotas(applicationContext, R.layout.activity_adapter_list_view, petsArray)
                listPetsView.adapter = petList
            }
            .addOnFailureListener { exception ->
                val emptyTV = findViewById<TextView>(R.id.EmptyViewTV)
                val listPetsView = findViewById<ListView>(R.id.PetsLV)
                listPetsView.emptyView = emptyTV
            }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (menu is MenuBuilder) menu.setOptionalIconsVisible(true)
        menuInflater.inflate(R.menu.top_bar_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOut -> {
                val i = Intent(applicationContext,Login::class.java)
                startActivity(i)
            }
            R.id.contactUs -> {
                composeEmail("rfernandez@centronelson.org")
            }
            R.id.aboutMe -> {
                openWebPage("https://www.linkedin.com/in/rodrigo-fernandez-alonso/")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun composeEmail(address: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$address") // Only email apps handle this.
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

}