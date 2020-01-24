package br.com.main.modules.main.model

import com.google.firebase.database.Exclude

class Campeonato() {

    lateinit var nome: String
    lateinit var descricao: String
    lateinit var tipo: String
    lateinit var participantes: List<Participante>
    lateinit var quantidadeParticipantes: Number
    var dono: String? = ""
    lateinit var uid: String

    constructor(nome: String,
                descricao: String,
                tipo: String,
                quantidadeParticipantes: Int,
                dono: String?) : this() {
        this.nome = nome
        this.descricao = descricao
        this.tipo = tipo
        this.quantidadeParticipantes = quantidadeParticipantes
        this.dono = dono
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "nome" to nome,
                "descricao" to descricao,
                "tipo" to tipo,
                "quantidadeParticipantes" to quantidadeParticipantes,
                "dono" to dono,
                "uid" to uid
        )
    }

    @Exclude
    fun participantesToMap(): Map<String, Any?> {
        return mapOf(
                "participantes" to participantes
        )
    }
}