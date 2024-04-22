package com.example.recetasapp
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DetallesRecetaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_receta)

        // Obtener el ID de la receta pasado desde MainActivity usando intent.extras
        val recipeId = intent.getIntExtra("recipe_id", -1)

        // Realizar la solicitud para obtener los pasos de la receta
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val jsonString = getRequest("https://api.spoonacular.com/recipes/$recipeId/analyzedInstructions?apiKey=3d09d02a67804174991ffe322714f9ed")
                val response = Gson().fromJson(jsonString, Array<RecipeInstructionsResponse>::class.java)

                // Procesar y mostrar los pasos de la receta en la interfaz de usuario
                runOnUiThread {
                    displayRecipeSteps(response)
                }
            } catch (e: Exception) {
                println("Error al obtener los pasos de la receta: ${e.message}")
            }
        }
    }

    private fun displayRecipeSteps(stepsResponse: Array<RecipeInstructionsResponse>) {
        // Si la respuesta contiene datos y pasos
        if (stepsResponse.isNotEmpty()) {
            val steps = stepsResponse[0].steps
            val stepsText = steps.joinToString("\n") { "${it.number}. ${it.step}" }

            // Mostrar los pasos en un TextView en tu diseño
            findViewById<TextView>(R.id.textViewInstrucciones).text = stepsText
        } else {
            // Si no hay pasos disponibles, mostrar un mensaje indicando esto
            findViewById<TextView>(R.id.textViewInstrucciones).text = "No se encontraron instrucciones para esta receta."
        }
    }

    // Función para realizar la solicitud HTTP GET
    private fun getRequest(urlString: String): String {
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
    data class RecipeInstructionsResponse(
        val name: String,
        val steps: List<Step>
    )

    data class Step(
        val number: Int,
        val step: String
    )

}
