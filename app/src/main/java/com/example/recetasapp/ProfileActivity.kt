package com.example.recetasapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView


class ProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var switchLactosa: Switch
    private lateinit var switchGluten: Switch
    private lateinit var switchMariscos: Switch

    private lateinit var cardVegan: CardView
    private lateinit var cardVegetariana: CardView
    private lateinit var cardKeto: CardView
    private lateinit var cardPaleo: CardView

    private var currentlySelectedCard: CardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)

        // Initialize switches
        switchLactosa = findViewById(R.id.toggleButton1)
        switchGluten = findViewById(R.id.toggleButton2)
        switchMariscos = findViewById(R.id.toggleButton3)

        // Load switch states from SharedPreferences
        switchLactosa.isChecked = sharedPreferences.getBoolean("lactosa", false)
        switchGluten.isChecked = sharedPreferences.getBoolean("gluten", false)
        switchMariscos.isChecked = sharedPreferences.getBoolean("mariscos", false)

        // Set listeners for switches
        switchLactosa.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("lactosa", isChecked).apply()
        }
        switchGluten.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("gluten", isChecked).apply()
        }
        switchMariscos.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("mariscos", isChecked).apply()
        }

        // Initialize cards
        cardVegan = findViewById(R.id.cardVegan)
        cardVegetariana = findViewById(R.id.cardVegetariana)
        cardKeto = findViewById(R.id.cardKeto)
        cardPaleo = findViewById(R.id.cardPaleo)

        // Set click listeners for cards
        cardVegan.setOnClickListener { toggleCardSelection(cardVegan) }
        cardVegetariana.setOnClickListener { toggleCardSelection(cardVegetariana) }
        cardKeto.setOnClickListener { toggleCardSelection(cardKeto) }
        cardPaleo.setOnClickListener { toggleCardSelection(cardPaleo) }

        // Load selected card from SharedPreferences
        val selectedCard = sharedPreferences.getString("selectedCard", null)
        if (selectedCard != null) {
            currentlySelectedCard = when (selectedCard) {
                "vegan" -> cardVegan
                "vegetariana" -> cardVegetariana
                "keto" -> cardKeto
                "paleo" -> cardPaleo
                else -> null
            }
            currentlySelectedCard?.setCardBackgroundColor(resources.getColor(R.color.colorTarjetaSeleccionada))
        }
    }

    private fun toggleCardSelection(selectedCard: CardView) {
        if (currentlySelectedCard == selectedCard) {
            // Deselect the currently selected card
            selectedCard.setCardBackgroundColor(resources.getColor(R.color.white))
            sharedPreferences.edit().putString("selectedCard", null).apply()
            currentlySelectedCard = null
        } else {
            // Select the new card
            currentlySelectedCard?.setCardBackgroundColor(resources.getColor(R.color.white))
            selectedCard.setCardBackgroundColor(resources.getColor(R.color.colorTarjetaSeleccionada))
            val selectedCardId = when (selectedCard.id) {
                R.id.cardVegan -> "vegan"
                R.id.cardVegetariana -> "vegetariana"
                R.id.cardKeto -> "keto"
                R.id.cardPaleo -> "paleo"
                else -> ""
            }
            sharedPreferences.edit().putString("selectedCard", selectedCardId).apply()
            currentlySelectedCard = selectedCard
        }
    }
}