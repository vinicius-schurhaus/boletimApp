package udesc.boletimapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import udesc.boletimapp.databinding.ActivityAlunoBinding
import udesc.boletimapp.databinding.ActivityMainBinding

class AlunoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlunoBinding
    private lateinit var adapter: ArrayAdapter<Aluno>
    private var pos: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAlunoBinding.inflate(layoutInflater)
        setContentView(binding.main)

        val db = DBHelper(this)
        val listaAlunos = db.alunosSelectAll()

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaAlunos
        )
        binding.listView.adapter = adapter

        binding.listView.setOnItemClickListener { _, _, i, _ ->
            binding.textId.setText("ID: ${listaAlunos[i].id}")
            binding.editNomeAluno.setText(listaAlunos[i].nome)
            binding.editMatricula.setText(listaAlunos[i].matricula)
            pos = i
        }

        binding.buttonInsert.setOnClickListener {
            val nome = binding.editNomeAluno.text.toString()
            val matricula = binding.editMatricula.text.toString()

            val resultado = db.alunoInsert(nome, matricula)
            if (resultado > 0) {
                Toast.makeText(applicationContext, "Sucesso! Id: $resultado", Toast.LENGTH_SHORT)
                    .show()
                listaAlunos.add(Aluno(resultado.toInt(), nome, matricula))
                adapter.notifyDataSetChanged()
                binding.textId.setText("ID: ")
                binding.editNomeAluno.setText("")
                binding.editMatricula.setText("")
            } else {
                Toast.makeText(applicationContext, "Falha na inserção!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonUpdate.setOnClickListener {
            if (pos >= 0) {
                val id = listaAlunos[pos].id
                val nome = binding.editNomeAluno.text.toString()
                val matricula = binding.editMatricula.text.toString()
                val resultado = db.alunoUpdate(id, nome, matricula)

                if (resultado > 0) {
                    Toast.makeText(applicationContext, "Registro atualizado!", Toast.LENGTH_SHORT)
                        .show()
                    listaAlunos[pos].nome = nome
                    listaAlunos[pos].matricula = matricula
                    adapter.notifyDataSetChanged()
                    pos = -1
                    binding.textId.setText("ID: ")
                    binding.editNomeAluno.setText("")
                    binding.editMatricula.setText("")
                } else {
                    Toast.makeText(applicationContext, "Falha na atualização!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.buttonDelete.setOnClickListener {
            if (pos >= 0) {
                val id = listaAlunos[pos].id

                val notasDoAluno = db.notaSelectAll().filter { it.alunoId == id }

                if (notasDoAluno.isNotEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "O aluno possui notas associadas e não pode ser excluído!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val resultado = db.alunoDelete(id)

                    if (resultado > 0) {
                        Toast.makeText(applicationContext, "Registro removido!", Toast.LENGTH_SHORT)
                            .show()
                        listaAlunos.removeAt(pos)
                        adapter.notifyDataSetChanged()
                        pos = -1
                        binding.textId.setText("ID: ")
                        binding.editNomeAluno.setText("")
                        binding.editMatricula.setText("")
                    } else {
                        Toast.makeText(applicationContext, "Falha na exclusão!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
    }
}