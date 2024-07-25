package com.example.recetasapp

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class BuscadorPorIngredientesActivity : AppCompatActivity() {

    private val listaIngredientes = mutableListOf<String>()
    private lateinit var layoutTarjetasIngredientes: LinearLayout
    private var tiempoSeleccionado: Int? = null // Variable para almacenar el tiempo seleccionado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscador_recetas)

        findViewById<RelativeLayout>(R.id.btnBuscar).setOnClickListener {

        }

        var isCardPressed = false
        findViewById<RelativeLayout>(R.id.tarjeta10).setOnClickListener {
            isCardPressed = !isCardPressed // Alternar el estado
            // Aplicar el fondo según el estado
            val backgroundDrawable = if (isCardPressed) {
                R.drawable.rounded_card_pressed
            } else {
                R.drawable.rounded_card
            }
            it.setBackgroundResource(backgroundDrawable)
        }

        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val editIngredientes = findViewById<EditText>(R.id.editIngredientes)
        layoutTarjetasIngredientes = findViewById(R.id.layoutTarjetasIngredientes)

        btnAgregar.setOnClickListener {
            val ingrediente = editIngredientes.text.toString().trim()

            if (ingrediente.isNotEmpty()) {
                listaIngredientes.add(ingrediente)
                editIngredientes.text.clear()
                agregarTarjeta(ingrediente)
            }
        }
    }


    private fun agregarTarjeta(ingrediente: String) {
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.HORIZONTAL

        val textView = TextView(this).apply {
            text = ingrediente
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
            setBackgroundResource(R.drawable.edittext_rounded)
            setPadding(16, 0, 16, 8)
            measure(0, 0)
            val width = measuredWidth
            val height = measuredHeight
        }
        val colorTextoX = Color.parseColor("#504646")
        val btnEliminar = Button(this).apply {
            text = "X"
            layoutParams = LinearLayout.LayoutParams(
                textView.measuredHeight,
                textView.measuredHeight,
                1.0f
            )
            setBackgroundResource(R.drawable.button_rounded)
            setPadding(16, 0, 16, 8)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.5f)
            setTextColor(colorTextoX)
            setOnClickListener {
                listaIngredientes.remove(ingrediente)
                layoutTarjetasIngredientes.removeView(linearLayout)
            }
        }

        linearLayout.addView(textView)
        linearLayout.addView(btnEliminar)

        layoutTarjetasIngredientes.addView(linearLayout)

        if (layoutTarjetasIngredientes.childCount > 1) {
            val params = linearLayout.layoutParams as LinearLayout.LayoutParams
            params.setMargins(16, 0, 0, 0)
            linearLayout.layoutParams = params
        }
    }

}
