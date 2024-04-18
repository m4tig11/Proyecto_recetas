package com.example.recetasapp

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados)

        // Llamamos a la función para obtener las recetas
        obtenerRecetas()
    }

    private fun obtenerRecetas() {
        // Aquí iría la llamada a la API y el procesamiento de los datos
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val jsonString = getRequest("https://api.spoonacular.com/recipes/complexSearch?apiKey=3d09d02a67804174991ffe322714f9ed&includeIngredients=Chicken,rice&instructionsRequired=true&number=2")
                val response = Gson().fromJson(jsonString, ApiResponse::class.java)

                // Ahora puedes trabajar con la lista de recetas obtenida
                response.results.forEach { recipe ->
                    runOnUiThread {
                        // Aquí actualizas la interfaz de usuario con los datos de la receta
                        val cardView = createCardViewForRecipe(recipe)
                        findViewById<LinearLayout>(R.id.contenedorTarjetas).addView(cardView)
                    }
                }
            } catch (e: Exception) {
                println("Error al obtener las recetas: ${e.message}")
            }
        }
    }

    private fun createCardViewForRecipe(recipe: Recipe): CardView {
        val cardView = CardView(this@MainActivity)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(16, 16, 16, 0) // Margen superior para separar las tarjetas
        cardView.layoutParams = layoutParams
        cardView.radius = 8f // Radio de las esquinas de la tarjeta
        cardView.cardElevation = 8f // Elevación de la tarjeta

        // Crear un RelativeLayout para la tarjeta
        val relativeLayout = RelativeLayout(this@MainActivity)
        relativeLayout.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        relativeLayout.setBackgroundColor(Color.WHITE) // Fondo blanco para la tarjeta

        // Crear un ImageView para la imagen de la receta
        // Crear un ImageView para la imagen de la receta
        val imageView = ImageView(this@MainActivity)
        imageView.id = View.generateViewId() // Asignar un ID único al ImageView
        val imageViewParams = RelativeLayout.LayoutParams(
            120.dpToPx(this@MainActivity), // Ancho de la imagen (en píxeles)
            120.dpToPx(this@MainActivity) // Altura de la imagen (en píxeles)
        )
        imageViewParams.addRule(RelativeLayout.ALIGN_PARENT_START)
        imageViewParams.addRule(RelativeLayout.CENTER_VERTICAL)
        imageView.layoutParams = imageViewParams
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

// Cargar la imagen de la receta utilizando Picasso
        Picasso.get().load(recipe.image).into(imageView)

// Agregar el ImageView al RelativeLayout
        relativeLayout.addView(imageView)

// Crear un TextView para el nombre de la receta
        val textView = TextView(this@MainActivity)
        val textViewParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT, // Ancho envuelto al contenido
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        textViewParams.addRule(RelativeLayout.RIGHT_OF, imageView.id) // Alinear a la derecha del ImageView
        textViewParams.addRule(RelativeLayout.ALIGN_TOP, imageView.id) // Alinear al inicio vertical del ImageView
        textViewParams.leftMargin = 16.dpToPx(this@MainActivity) // Margen izquierdo
        textViewParams.rightMargin = 16.dpToPx(this@MainActivity) // Margen derecho para separar la imagen del texto
        textView.layoutParams = textViewParams
        textView.text = recipe.title // Establecer el texto del TextView como el nombre de la receta
        textView.setTextColor(Color.BLACK)
        textView.textSize = 18f // Tamaño de texto
        textView.setTypeface(null, Typeface.BOLD) // Texto en negrita

// Agregar el TextView al RelativeLayout
        relativeLayout.addView(textView)





        // Agregar el RelativeLayout (tarjeta) al CardView
        cardView.addView(relativeLayout)

        return cardView
    }
}

// Extensión para convertir dp a píxeles
private fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}

data class ApiResponse(
    val results: List<Recipe>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
)

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String
)


// Función para realizar la solicitud HTTP GET
fun getRequest(urlString: String): String {
    val url = URL(urlString)
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"

    val responseCode = connection.responseCode
    if (responseCode == HttpURLConnection.HTTP_OK) {
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        reader.close()
        return response.toString()
    } else {
        throw Exception("Error en la solicitud HTTP: $responseCode")
    }
}
