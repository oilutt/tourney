package br.com.main.modules.main.repositories.implementation

import br.com.main.modules.main.model.Campeonato
import br.com.main.modules.main.model.Participante
import br.com.main.modules.main.repositories.interfaces.CampeonatosRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await

class CampeonatosRepositoryImpl(val auth: FirebaseAuth, val firebaseFirestore: FirebaseFirestore) : CampeonatosRepository {

    val CAMPEONATO = "campeonatos"
    val PARTICIPANTES = "participantes"
    val USERS = "usuarios"

    override suspend fun saveCamp(campeonato: Campeonato): Deferred<Void> {
        val ref = firebaseFirestore.collection(CAMPEONATO)
        campeonato.uid = ref.id
        return ref.document(campeonato.uid)
            .set(campeonato.toMap()).asDeferred()
    }

    override suspend fun saveCampByUser(campeonato: Campeonato): Deferred<Void> {
        return firebaseFirestore.collection(USERS)
            .document(auth.currentUser!!.uid)
            .collection(CAMPEONATO)
            .document(campeonato.uid)
            .set(campeonato.toMap()).asDeferred()
    }

    override suspend fun saveParticipants(campeonato: Campeonato): Deferred<Void> {
        return firebaseFirestore.collection(PARTICIPANTES)
            .document(campeonato.uid)
            .set(campeonato.participantesToMap()).asDeferred()
    }

    override suspend fun getMyCamps(): Deferred<QuerySnapshot> {
        return firebaseFirestore.collection(USERS).document(auth.currentUser!!.uid).collection(CAMPEONATO).get().asDeferred()
    }
}