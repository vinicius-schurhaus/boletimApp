package udesc.boletimapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import udesc.boletimapp.databinding.ActivityAlunoBinding
import udesc.boletimapp.databinding.ActivityDisciplinaBinding

class DisciplinaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisciplinaBinding
    private lateinit var adapter: ArrayAdapter<Disciplina>
    private var pos: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDisciplinaBinding.inflate(layoutInflater)
        setContentView(binding.main)

        val db = DBHelper(this)
        val listaDisciplinas = db.disciplinaSelectAll()

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaDisciplinas
        )
        binding.listView.adapter = adapter

        binding.listView.setOnItemClickListener { _, _, i, _ ->
            binding.textId.setText("ID: ${listaDisciplinas[i].id}")
            binding.editNomeDisciplina.setText(listaDisciplinas[i].nome)
            binding.editProfessor.setText(listaDisciplinas[i].professor)
            pos = i
        }

        binding.buttonInsert.setOnClickListener {
            val nome = binding.editNomeDisciplina.text.toString()
            val professor = binding.editProfessor.text.toString()

            val resultado = db.disciplinaInsert(nome, professor)
            if (resultado > 0) {
                Toast.makeText(applicationContext, "Sucesso! Id: $resultado", Toast.LENGTH_SHORT)
                    .show()
                listaDisciplinas.add(Disciplina(resultado.toInt(), nome, professor))
                adapter.notifyDataSetChanged()
                binding.textId.setText("ID: ")
                binding.editNomeDisciplina.setText("")
                binding.editProfessor.setText("")
            } else {
                Toast.makeText(applicationContext, "Falha na inserção!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonUpdate.setOnClickListener {
            if (pos >= 0) {
                val id = listaDisciplinas[pos].id
                val nome = binding.editNomeDisciplina.text.toString()
                val professor = binding.editProfessor.text.toString()
                val resultado = db.disciplinaUpdate(id, nome, professor)

                if (resultado > 0) {
                    Toast.makeText(applicationContext, "Registro atualizado!", Toast.LENGTH_SHORT)
                        .show()
                    listaDisciplinas[pos].nome = nome
                    listaDisciplinas[pos].professor = professor
                    adapter.notifyDataSetChanged()
                    pos = -1
                    binding.textId.setText("ID: ")
                    binding.editNomeDisciplina.setText("")
                    binding.editProfessor.setText("")
                } else {
                    Toast.makeText(applicationContext, "Falha na atualização!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.buttonDelete.setOnClickListener {
            if (pos >= 0) {
                val id = listaDisciplinas[pos].id

                val notasDaDisciplina = db.notaSelectAll().filter { it.disciplinaId == id }

                if (notasDaDisciplina.isNotEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "A disciplina possui notas associadas e não pode ser excluída!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val resultado = db.disciplinaDelete(id)

                    if (resultado > 0) {
                        Toast.makeText(applicationContext, "Registro removido!", Toast.LENGTH_SHORT)
                            .show()
                        listaDisciplinas.removeAt(pos)
                        adapter.notifyDataSetChanged()
                        pos = -1
                        binding.textId.setText("ID: ")
                        binding.editNomeDisciplina.setText("")
                        binding.editProfessor.setText("")
                    } else {
                        Toast.makeText(applicationContext, "Falha na exclusão!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}