package udesc.boletimapp

class Aluno (val id: Int = 0, var nome: String = "", var matricula: String = "") {

    override fun toString(): String {
        return "$id. $nome, matrícula: $matricula."
    }
}