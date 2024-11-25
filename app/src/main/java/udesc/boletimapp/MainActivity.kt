package udesc.boletimapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import udesc.boletimapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)

        binding.buttonAlunos.setOnClickListener {
            startActivity(Intent(this, AlunoActivity::class.java))
        }

        binding.buttonDisciplinas.setOnClickListener {
            startActivity(Intent(this, DisciplinaActivity::class.java))
        }

        binding.buttonNotas.setOnClickListener {
            startActivity(Intent(this, NotaActivity::class.java))
        }
    }
}