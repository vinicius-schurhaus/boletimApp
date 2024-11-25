package udesc.boletimapp

class Nota(val id: Int = 0, var alunoId: Int = 0, var disciplinaId: Int = 0, var nota: Double = 0.0) {

    override fun toString(): String {
        return "Nota $id: ($alunoId, $disciplinaId) = $nota"
    }
}
