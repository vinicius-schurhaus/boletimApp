package udesc.boletimapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "banco.db", null, 1) {

    val sql = arrayOf(
        "CREATE TABLE alunos (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, matricula TEXT)",
        "CREATE TABLE disciplinas (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, professor TEXT)",
        "CREATE TABLE notas (id INTEGER PRIMARY KEY AUTOINCREMENT, alunoId INTEGER, disciplinaId INTEGER, nota REAL)",
        "INSERT INTO alunos (nome, matricula) VALUES ('Thayná Guimarães Santana', '123123')",
        "INSERT INTO alunos (nome, matricula) VALUES ('Matheus de Britto', '456456')",
        "INSERT INTO disciplinas (nome, professor) VALUES ('Programação Front-End', 'Ricardo Leardini Lobo')",
        "INSERT INTO disciplinas (nome, professor) VALUES ('Interação Humano-Computador', 'Igor Reszka Pinheiro')"
    )

    override fun onCreate(db: SQLiteDatabase?) {
        sql.forEach {
            if (db != null) {
                db.execSQL(it)
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS alunos")
        db.execSQL("DROP TABLE IF EXISTS disciplinas")
        db.execSQL("DROP TABLE IF EXISTS notas")
        onCreate(db)
    }

    fun alunosSelectById(id: Int): Aluno {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM alunos WHERE id = ?", arrayOf(id.toString()))
        var aluno = Aluno()
        if (c.count == 1) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val nomeIndex = c.getColumnIndex("nome")
            val matriculaIndex = c.getColumnIndex("matricula")

            val id = c.getInt(idIndex)
            val nome = c.getString(nomeIndex)
            val matricula = c.getString(matriculaIndex)

            aluno = Aluno(id, nome, matricula)
        }
        c.close()
        return aluno
    }

    fun alunosSelectAll(): ArrayList<Aluno> {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM alunos", null)
        val listaAlunos = ArrayList<Aluno>()
        if (c.count > 0) {
            c.moveToFirst()
            do {
                val idIndex = c.getColumnIndex("id")
                val nomeIndex = c.getColumnIndex("nome")
                val matriculaIndex = c.getColumnIndex("matricula")

                val id = c.getInt(idIndex)
                val nome = c.getString(nomeIndex)
                val matricula = c.getString(matriculaIndex)

                listaAlunos.add(Aluno(id, nome, matricula))
            } while (c.moveToNext())
        }
        c.close()
        return listaAlunos
    }


    fun alunoInsert(nome: String, matricula: String) : Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nome", nome)
        contentValues.put("matricula", matricula)
        val resultado = db.insert("alunos", null, contentValues)
        db.close()
        return resultado
    }

    fun alunoUpdate(id: Int, nome: String, matricula: String) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nome", nome)
        contentValues.put("matricula", matricula)
        val resultado = db.update("alunos", contentValues, "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }

    fun alunoDelete(id: Int) : Int {
        val db = this.writableDatabase
        val resultado = db.delete("alunos", "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }

    fun disciplinaSelectById(id: Int): Disciplina? {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM disciplinas WHERE id = ?", arrayOf(id.toString()))
        var disciplina: Disciplina? = null
        if (c.moveToFirst()) {
            val idIndex = c.getColumnIndex("id")
            val nomeIndex = c.getColumnIndex("nome")
            val professorIndex = c.getColumnIndex("professor")
            val id = c.getInt(idIndex)
            val nome = c.getString(nomeIndex)
            val professor = c.getString(professorIndex)
            disciplina = Disciplina(id, nome, professor)
        }
        c.close()
        return disciplina
    }

    fun disciplinaSelectAll(): ArrayList<Disciplina> {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM disciplinas", null)
        val listaDisciplinas = ArrayList<Disciplina>()
        if (c.moveToFirst()) {
            do {
                val idIndex = c.getColumnIndex("id")
                val nomeIndex = c.getColumnIndex("nome")
                val professorIndex = c.getColumnIndex("professor")
                val id = c.getInt(idIndex)
                val nome = c.getString(nomeIndex)
                val professor = c.getString(professorIndex)
                listaDisciplinas.add(Disciplina(id, nome, professor))
            } while (c.moveToNext())
        }
        c.close()
        return listaDisciplinas
    }

    fun disciplinaInsert(nome: String, professor: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("nome", nome)
            put("professor", professor)
        }
        val result = db.insert("disciplinas", null, contentValues)
        db.close()
        return result
    }

    fun disciplinaUpdate(id: Int, nome: String, professor: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("nome", nome)
            put("professor", professor)
        }
        val result = db.update("disciplinas", contentValues, "id = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun disciplinaDelete(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete("disciplinas", "id = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun notaSelectById(id: Int): Nota? {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM notas WHERE id = ?", arrayOf(id.toString()))
        var nota: Nota? = null
        if (c.moveToFirst()) {
            val idIndex = c.getColumnIndex("id")
            val alunoIdIndex = c.getColumnIndex("alunoId")
            val disciplinaIdIndex = c.getColumnIndex("disciplinaId")
            val notaIndex = c.getColumnIndex("nota")
            val id = c.getInt(idIndex)
            val alunoId = c.getInt(alunoIdIndex)
            val disciplinaId = c.getInt(disciplinaIdIndex)
            val notaValor = c.getDouble(notaIndex)
            nota = Nota(id, alunoId, disciplinaId, notaValor)
        }
        c.close()
        return nota
    }

    fun notaSelectAll(): ArrayList<Nota> {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM notas", null)
        val listaNotas = ArrayList<Nota>()
        if (c.moveToFirst()) {
            do {
                val idIndex = c.getColumnIndex("id")
                val alunoIdIndex = c.getColumnIndex("alunoId")
                val disciplinaIdIndex = c.getColumnIndex("disciplinaId")
                val notaIndex = c.getColumnIndex("nota")
                val id = c.getInt(idIndex)
                val alunoId = c.getInt(alunoIdIndex)
                val disciplinaId = c.getInt(disciplinaIdIndex)
                val notaValor = c.getDouble(notaIndex)
                listaNotas.add(Nota(id, alunoId, disciplinaId, notaValor))
            } while (c.moveToNext())
        }
        c.close()
        return listaNotas
    }

    fun notaInsert(alunoId: Int, disciplinaId: Int, nota: Double): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("alunoId", alunoId)
            put("disciplinaId", disciplinaId)
            put("nota", nota)
        }
        val result = db.insert("notas", null, contentValues)
        db.close()
        return result
    }

    fun notaUpdate(id: Int, alunoId: Int, disciplinaId: Int, nota: Double): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("alunoId", alunoId)
            put("disciplinaId", disciplinaId)
            put("nota", nota)
        }
        val result = db.update("notas", contentValues, "id = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun notaDelete(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete("notas", "id = ?", arrayOf(id.toString()))
        db.close()
        return result
    }
}