package com.example.recetasapp
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class ResultadosActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados)

        val arrayIngredientes = intent.getStringArrayExtra("Lista_ingredientes")
        val maxReadytime = intent.getStringExtra("MaxReadyTime")
        val typeDish = intent.getStringExtra("TypeDish")
        Log.d("array", arrayIngredientes.toString())
        val listaIngredientes = arrayIngredientes?.toMutableList() ?: mutableListOf()
        Log.d("lista", listaIngredientes.toString())
        val strIngredientes = CrearStringredientes(listaIngredientes)
        Log.d("str", strIngredientes.toString())
        Log.d("tiempo", maxReadytime.toString())

        obtenerRecetas(strIngredientes, maxReadytime.toString(),typeDish.toString())

    }
    private fun CrearStringredientes(listaIngredientes: List<String>): StringBuilder {
        var strBuilder = StringBuilder()
        listaIngredientes.forEach{ingrediente ->
            strBuilder.append(ingrediente).append(",")
            Log.d("ingredientE", ingrediente.toString())
        }
        if (strBuilder.isNotEmpty()) {
            strBuilder.deleteCharAt(strBuilder.length - 1)  // Eliminar la última coma
        }
        Log.d("strBuilder",strBuilder.toString())
        return strBuilder

    }
    private fun obtenerRecetas(strIngredientes: StringBuilder,time: String,type: String) {
        // Aquí iría la llamada a la API y el procesamiento de los datos
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val urlGet = "https://api.spoonacular.com/recipes/complexSearch?apiKey=3d09d02a67804174991ffe322714f9ed&includeIngredients=$strIngredientes&instructionsRequired=true&number=6&type=$type&maxReadyTime=$time"
                Log.i("url", urlGet)
                val jsonString = getRequest(urlGet)
                val response = Gson().fromJson(jsonString, ApiResponse::class.java)

                // Ahora puedes trabajar con la lista de recetas obtenida
                response.results.forEach { recipe ->
                    runOnUiThread {
                        val cardView = createCardViewForRecipe(recipe)
                        cardView.setOnClickListener {
                            // Al hacer clic en la tarjeta, abrir la nueva actividad con los detalles de la receta
                            val intent = Intent(this@ResultadosActivity, DetallesRecetaActivity::class.java)
                            intent.putExtra("recipe_id", recipe.id)
                            intent.putExtra("recipe_image", recipe.image)
                            intent.putExtra("recipe_title", recipe.title)
                            startActivity(intent)
                        }
                        findViewById<LinearLayout>(R.id.contenedorTarjetas).addView(cardView)
                    }
                }
            } catch (e: Exception) {
                println("Error al obtener las recetas: ${e.message}")
            }
    }
}

    private fun createCardViewForRecipe(recipe: Recipe): CardView {
        val cardView = CardView(this@ResultadosActivity)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(16, 16, 16, 0) // Margen superior para separar las tarjetas
        cardView.layoutParams = layoutParams
        cardView.radius = 8f // Radio de las esquinas de la tarjeta
        cardView.cardElevation = 8f // Elevación de la tarjeta

        // Crear un RelativeLayout para la tarjeta
        val relativeLayout = RelativeLayout(this@ResultadosActivity)
        relativeLayout.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        relativeLayout.setBackgroundColor(Color.WHITE) // Fondo blanco para la tarjeta

        // Crear un ImageView para la imagen de la receta
        val imageView = ImageView(this@ResultadosActivity)
        imageView.id = View.generateViewId() // Asignar un ID único al ImageView
        val imageViewParams = RelativeLayout.LayoutParams(
            120.dpToPx(this@ResultadosActivity), // Ancho de la imagen (en píxeles)
            120.dpToPx(this@ResultadosActivity) // Altura de la imagen (en píxeles)
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
        val textView = TextView(this@ResultadosActivity)
        val textViewParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT, // Ancho envuelto al contenido
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        textViewParams.addRule(RelativeLayout.RIGHT_OF, imageView.id) // Alinear a la derecha del ImageView
        textViewParams.addRule(RelativeLayout.ALIGN_TOP, imageView.id) // Alinear al inicio vertical del ImageView
        textViewParams.leftMargin = 16.dpToPx(this@ResultadosActivity) // Margen izquierdo
        textViewParams.rightMargin = 16.dpToPx(this@ResultadosActivity) // Margen derecho para separar la imagen del texto
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
