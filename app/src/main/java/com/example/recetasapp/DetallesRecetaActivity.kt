import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recetasapp.R

class DetallesRecetaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_receta)

        // Aquí puedes agregar la lógica para mostrar los detalles de la receta
        // Puedes obtener el ID de la receta pasado desde MainActivity usando intent.extras
        // Luego, puedes realizar una llamada a la API para obtener más detalles de la receta
        // y mostrarlos en la interfaz de usuario
    }
}
