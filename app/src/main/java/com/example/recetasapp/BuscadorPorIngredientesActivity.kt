package com.example.recetasapp

import android.content.Intent
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
import android.util.Log
import androidx.core.content.ContextCompat

class BuscadorPorIngredientesActivity : AppCompatActivity() {

    private val listaIngredientes = mutableListOf<String>()
    private lateinit var layoutTarjetasIngredientes: LinearLayout
    private var maxReadyTime: String = "300"
    private var type: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscador_recetas)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)

        val editIngredientes = findViewById<EditText>(R.id.editIngredientes)
        layoutTarjetasIngredientes = findViewById(R.id.layoutTarjetasIngredientes)

        val tarjeta10 = findViewById<RelativeLayout>(R.id.tarjeta10)
        val tarjeta25 = findViewById<RelativeLayout>(R.id.tarjeta25)
        val tarjeta45 = findViewById<RelativeLayout>(R.id.tarjeta45)
        val tarjeta60 = findViewById<RelativeLayout>(R.id.tarjeta60)

        tarjeta10.setOnClickListener {
            maxReadyTime = "10"
            tarjeta10.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color10)
            tarjeta25.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color25)
            tarjeta45.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color45)
            tarjeta60.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color60)

            tarjeta10.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorTarjetaSeleccionada)


        }

        tarjeta25.setOnClickListener {
            maxReadyTime = "25"
            tarjeta10.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color10)
            tarjeta25.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color25)
            tarjeta45.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color45)
            tarjeta60.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color60)

            tarjeta25.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorTarjetaSeleccionada)

        }

        tarjeta45.setOnClickListener {
            maxReadyTime = "45"
            tarjeta10.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color10)
            tarjeta25.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color25)
            tarjeta45.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color45)
            tarjeta60.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color60)

            tarjeta45.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorTarjetaSeleccionada)

        }

        tarjeta60.setOnClickListener {
            maxReadyTime = "60"
            tarjeta10.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color10)
            tarjeta25.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color25)
            tarjeta45.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color45)
            tarjeta60.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color60)

            tarjeta60.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorTarjetaSeleccionada)
        }

        val tarjetaDes = findViewById<RelativeLayout>(R.id.tarjetaDes)
        val tarjetaAlm = findViewById<RelativeLayout>(R.id.tarjetaAlm)
        val tarjetaCen = findViewById<RelativeLayout>(R.id.tarjetaCen)
        val tarjetaPos = findViewById<RelativeLayout>(R.id.tarjetaPos)

        tarjetaDes.setOnClickListener {
            type = "breakfast"
            tarjetaDes.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorTarjetaSeleccionada)
            tarjetaAlm.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            tarjetaCen.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            tarjetaPos.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)

        }

        tarjetaAlm.setOnClickListener {
            type = "main course"
            tarjetaDes.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            tarjetaAlm.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorTarjetaSeleccionada)
            tarjetaCen.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            tarjetaPos.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
        }

        tarjetaCen.setOnClickListener {
            type = "main course"
            tarjetaDes.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            tarjetaAlm.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            tarjetaCen.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorTarjetaSeleccionada)
            tarjetaPos.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
        }

        tarjetaPos.setOnClickListener {
            type = "dessert"
            tarjetaDes.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            tarjetaAlm.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            tarjetaCen.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            tarjetaPos.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorTarjetaSeleccionada)
        }


        btnAgregar.setOnClickListener {
            val ingrediente = editIngredientes.text.toString().trim()

            if (ingrediente.isNotEmpty()) {
                listaIngredientes.add(ingrediente)
                editIngredientes.text.clear()
                agregarTarjeta(ingrediente)
            }
        }

        btnBuscar.setOnClickListener {

            println(maxReadyTime)

            val intent = Intent(this@BuscadorPorIngredientesActivity, ResultadosActivity::class.java)
            intent.putExtra("Lista_ingredientes",listaIngredientes.toTypedArray())
            intent.putExtra("MaxReadyTime",maxReadyTime)
            intent.putExtra("TypeDish",type.toString())
            startActivity(intent)
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
