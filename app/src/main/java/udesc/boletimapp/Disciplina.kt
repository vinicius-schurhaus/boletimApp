package udesc.boletimapp

class Disciplina (val id: Int = 0, var nome: String = "", var professor: String = "") {

    override fun toString(): String {
        return "$id. $nome, prof. $professor"
    }
}