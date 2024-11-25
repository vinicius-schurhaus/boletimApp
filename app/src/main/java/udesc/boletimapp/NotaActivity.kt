package udesc.boletimapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.boletimapp.databinding.ActivityNotaBinding

class NotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotaBinding
    private lateinit var adapter: ArrayAdapter<Nota>
    private var pos: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this)
        val listaNotas = db.notaSelectAll()

        val listaExibicao = ArrayList<String>()

        listaNotas.forEach { nota ->
            val aluno = db.alunosSelectById(nota.alunoId)
            val disciplina = db.disciplinaSelectById(nota.disciplinaId)

            val alunoNome = aluno?.nome ?: "Aluno não encontrado"
            val disciplinaNome = disciplina?.nome ?: "Disciplina não encontrada"

            listaExibicao.add("Nota ${nota.id}: $alunoNome, $disciplinaNome = ${nota.nota}")
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaExibicao
        )

        binding.listView.adapter = adapter

        binding.listView.setOnItemClickListener { _, _, i, _ ->
            if (i >= 0 && i < listaNotas.size) {
                val selectedNota = listaNotas[i]
                binding.textId.setText("ID: ${selectedNota.id}")
                val aluno = db.alunosSelectById(selectedNota.alunoId)
                val disciplina = db.disciplinaSelectById(selectedNota.disciplinaId)
                binding.editNomeAluno.setText(aluno?.nome ?: "")
                binding.editNomeDisciplina.setText(disciplina?.nome ?: "")
                binding.editNota.setText(selectedNota.nota.toString())
                pos = i
            }
        }

        val listaAlunos = db.alunosSelectAll().map { it.nome }
        val alunoAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            listaAlunos
        )
        binding.editNomeAluno.setAdapter(alunoAdapter)

        val listaDisciplinas = db.disciplinaSelectAll().map { it.nome }
        val disciplinaAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            listaDisciplinas
        )
        binding.editNomeDisciplina.setAdapter(disciplinaAdapter)

        binding.buttonInsert.setOnClickListener {
            val nomeAluno = binding.editNomeAluno.text.toString()
            val nomeDisciplina = binding.editNomeDisciplina.text.toString()
            val nota = binding.editNota.text.toString().toDoubleOrNull()

            if (nota != null) {
                val aluno = db.alunosSelectAll().find { it.nome == nomeAluno }
                val disciplina = db.disciplinaSelectAll().find { it.nome == nomeDisciplina }

                if (aluno != null && disciplina != null) {
                    val resultado = db.notaInsert(aluno.id, disciplina.id, nota)
                    if (resultado > 0) {
                        Toast.makeText(applicationContext, "Sucesso! ID: $resultado", Toast.LENGTH_SHORT).show()

                        val listaNotasAtualizada = db.notaSelectAll()
                        listaNotas.clear()
                        listaNotas.addAll(listaNotasAtualizada)

                        val listaExibicaoAtualizada = listaNotas.map { nota ->
                            val aluno = db.alunosSelectById(nota.alunoId)
                            val disciplina = db.disciplinaSelectById(nota.disciplinaId)
                            val alunoNome = aluno?.nome ?: "Aluno não encontrado"
                            val disciplinaNome = disciplina?.nome ?: "Disciplina não encontrada"
                            "Nota ${nota.id}: $alunoNome, $disciplinaNome = ${nota.nota}"
                        }

                        adapter.clear()
                        adapter.addAll(listaExibicaoAtualizada)
                        adapter.notifyDataSetChanged()

                        binding.textId.setText("ID: ")
                        binding.editNomeAluno.setText("")
                        binding.editNomeDisciplina.setText("")
                        binding.editNota.setText("")
                        pos = -1
                    } else {
                        Toast.makeText(applicationContext, "Falha na inserção!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Aluno ou Disciplina não encontrados!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Nota inválida!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonUpdate.setOnClickListener {
            if (pos >= 0) {
                val id = listaNotas[pos].id
                val nomeAluno = binding.editNomeAluno.text.toString()
                val nomeDisciplina = binding.editNomeDisciplina.text.toString()
                val nota = binding.editNota.text.toString().toDoubleOrNull()

                if (nota != null) {
                    val aluno = db.alunosSelectAll().find { it.nome == nomeAluno }
                    val disciplina = db.disciplinaSelectAll().find { it.nome == nomeDisciplina }

                    if (aluno != null && disciplina != null) {
                        val resultado = db.notaUpdate(id, aluno.id, disciplina.id, nota)
                        if (resultado > 0) {
                            Toast.makeText(applicationContext, "Registro atualizado!", Toast.LENGTH_SHORT).show()

                            listaNotas[pos].nota = nota
                            listaNotas[pos].alunoId = aluno.id
                            listaNotas[pos].disciplinaId = disciplina.id

                            val listaExibicaoAtualizada = listaNotas.map { nota ->
                                val aluno = db.alunosSelectById(nota.alunoId)
                                val disciplina = db.disciplinaSelectById(nota.disciplinaId)
                                val alunoNome = aluno?.nome ?: "Aluno não encontrado"
                                val disciplinaNome = disciplina?.nome ?: "Disciplina não encontrada"
                                "Nota ${nota.id}: $alunoNome, $disciplinaNome = ${nota.nota}"
                            }

                            adapter.clear()
                            adapter.addAll(listaExibicaoAtualizada)
                            adapter.notifyDataSetChanged()

                            pos = -1
                            binding.textId.setText("ID: ")
                            binding.editNomeAluno.setText("")
                            binding.editNomeDisciplina.setText("")
                            binding.editNota.setText("")
                        } else {
                            Toast.makeText(applicationContext, "Falha na atualização!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Aluno ou Disciplina não encontrados!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Nota inválida!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.buttonDelete.setOnClickListener {
            if (pos >= 0) {
                val id = listaNotas[pos].id
                val resultado = db.notaDelete(id)

                if (resultado > 0) {
                    Toast.makeText(applicationContext, "Registro removido!", Toast.LENGTH_SHORT).show()

                    listaNotas.removeAt(pos)

                    val listaExibicaoAtualizada = listaNotas.map { nota ->
                        val aluno = db.alunosSelectById(nota.alunoId)
                        val disciplina = db.disciplinaSelectById(nota.disciplinaId)
                        val alunoNome = aluno?.nome ?: "Aluno não encontrado"
                        val disciplinaNome = disciplina?.nome ?: "Disciplina não encontrada"
                        "Nota ${nota.id}: $alunoNome, $disciplinaNome = ${nota.nota}"
                    }

                    adapter.clear()
                    adapter.addAll(listaExibicaoAtualizada)
                    adapter.notifyDataSetChanged()

                    pos = -1
                    binding.textId.setText("ID: ")
                    binding.editNomeAluno.setText("")
                    binding.editNomeDisciplina.setText("")
                    binding.editNota.setText("")
                } else {
                    Toast.makeText(applicationContext, "Falha na exclusão!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
