package br.com.main.modules.main.repositories.interfaces

import br.com.main.modules.main.model.Campeonato
import br.com.main.modules.main.model.Participante
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Deferred

interface CampeonatosRepository {

    suspend fun saveCamp(campeonato: Campeonato) : Deferred<Void>
    suspend fun saveCampByUser(campeonato: Campeonato) : Deferred<Void>
    suspend fun saveParticipants(campeonato: Campeonato) : Deferred<Void>
    suspend fun getMyCamps() : Deferred<QuerySnapshot>
}