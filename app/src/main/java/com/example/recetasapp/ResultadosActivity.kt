package com.example.recetasapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ResultadosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados)

        val arrayIngredientes = intent.getStringArrayExtra("Lista_ingredientes")
        val maxReadytime = intent.getStringExtra("MaxReadyTime")
        val typeDish = intent.getStringExtra("TypeDish")
        val lactosa = intent.getStringExtra("lactosa")
        val gluten = intent.getStringExtra("gluten")
        val mariscos = intent.getStringExtra("mariscos")
        val dieta = intent.getStringExtra("dieta")
        val listaIngredientes = arrayIngredientes?.toMutableList() ?: mutableListOf()
        val strIngredientes = CrearStringredientes(listaIngredientes)
        var strintolerancias = ""
        if (lactosa == "true") {
            strintolerancias += "lactosa,"
        }
        if (gluten == "true") {
            strintolerancias += "gluten,"
        }
        if (mariscos == "true") {
            strintolerancias += "mariscos,"
        }
        if (strintolerancias.isNotEmpty()) {
            strintolerancias = strintolerancias.removeSuffix(",")
        }

        obtenerRecetas(strIngredientes, maxReadytime.toString(), typeDish.toString(), dieta, strintolerancias)
    }

    private fun CrearStringredientes(listaIngredientes: List<String>): StringBuilder {
        val strBuilder = StringBuilder()
        listaIngredientes.forEach { ingrediente ->
            strBuilder.append(ingrediente).append(",")
            Log.d("ingredientE", ingrediente.toString())
        }
        if (strBuilder.isNotEmpty()) {
            strBuilder.deleteCharAt(strBuilder.length - 1)
        }
        Log.d("strBuilder", strBuilder.toString())
        return strBuilder
    }

    private fun obtenerRecetas(strIngredientes: StringBuilder, time: String, type: String, dieta: String?, intolerancias: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                var urlGet = "https://api.spoonacular.com/recipes/complexSearch?apiKey=3d09d02a67804174991ffe322714f9ed&includeIngredients=$strIngredientes&instructionsRequired=true&number=6&type=$type&maxReadyTime=$time&intolerances=$intolerancias"
                if (dieta != null) {
                    urlGet += "&diet=$dieta"
                }
                Log.i("url", urlGet)
                val jsonString = getRequest(urlGet)
                val response = Gson().fromJson(jsonString, ApiResponse::class.java)

                runOnUiThread {
                    val recyclerView = findViewById<RecyclerView>(R.id.contenedorTarjetas)
                    recyclerView.layoutManager = LinearLayoutManager(this@ResultadosActivity)
                    recyclerView.adapter = RecipeAdapter(this@ResultadosActivity, response.results)
                }
            } catch (e: Exception) {
                println("Error al obtener las recetas: ${e.message}")
            }
        }
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
