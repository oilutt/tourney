package br.com.main.modules.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import br.com.commons.CoroutineViewModel
import br.com.main.R
import br.com.main.modules.main.model.Campeonato
import br.com.main.modules.main.model.Participante
import br.com.main.modules.main.repositories.interfaces.CampeonatosRepository
import br.com.utils.DoubleMediatorLiveData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class CreateCampViewModel(
    val campeonatosRepository: CampeonatosRepository,
    val auth: FirebaseAuth
) : CoroutineViewModel() {

    val nomeCamp: MutableLiveData<String> = MutableLiveData()
    val descricao: MutableLiveData<String> = MutableLiveData()
    val tipoId: MutableLiveData<Int> = MutableLiveData()
    val quantidadeParticipantesId: MutableLiveData<Int> = MutableLiveData()
    private val tipo: MediatorLiveData<String> = MediatorLiveData()
    private val quantidadeParticipantes: MediatorLiveData<Int> = MediatorLiveData()

    private val participantesList: MutableLiveData<List<Participante>> = MutableLiveData()
    fun participantes() = participantesList as LiveData<List<Participante>>

    private val loading: MutableLiveData<Boolean> = MutableLiveData()
    fun loading() = loading as LiveData<Boolean>

    private val error: MutableLiveData<Throwable> = MutableLiveData()
    fun error() = error as LiveData<Throwable>

    private val sucess: MutableLiveData<Boolean> = MutableLiveData()
    fun sucess() = sucess as LiveData<Boolean>

    var continuarEnable = Transformations.switchMap(
        DoubleMediatorLiveData(
            DoubleMediatorLiveData(
                nomeCamp,
                descricao
            ), DoubleMediatorLiveData(tipo, quantidadeParticipantes)
        )
    ) {
        continuar(it.first, it.second)
    }!!

    init {
        tipo.addSource(tipoId) {
            when (it) {
                R.id.campeonato -> {
                    tipo.value = "Campeonato"
                }
                R.id.matamata -> {
                    tipo.value = "Mata-mata"
                }
                R.id.torneio -> {
                    tipo.value = "Torneio"
                }
            }
        }

        quantidadeParticipantes.addSource(quantidadeParticipantesId) {
            when (it) {
                R.id.oito -> {
                    quantidadeParticipantes.value = 8
                }
                R.id.dezesseis -> {
                    quantidadeParticipantes.value = 16
                }
                R.id.trintaedois -> {
                    quantidadeParticipantes.value = 32
                }
            }
            initParticipantesList()
        }
    }

    fun continuar(
        nomeEDescricao: Pair<String?, String?>?,
        tipoEQuantidadeParticipantes: Pair<String?, Int?>?
    ): LiveData<Boolean> {
        val retorno: MutableLiveData<Boolean> = MutableLiveData()
        if (nomeEDescricao != null && tipoEQuantidadeParticipantes != null) {
            retorno.value =
                !nomeEDescricao.first.isNullOrEmpty() && !nomeEDescricao.second.isNullOrEmpty() &&
                        !tipoEQuantidadeParticipantes.first.isNullOrEmpty() && tipoEQuantidadeParticipantes.second != null
        } else {
            retorno.value = false
        }
        return retorno
    }

    fun initParticipantesList() {
        val list: MutableList<Participante> = mutableListOf()
        for (i in (quantidadeParticipantes.value!! - 1) downTo 0 step 1) {
            list.add(Participante())
        }
        participantesList.value = list
    }

    fun createCamp(listParticipantes: List<Participante>? = null) {
        val campeonato = Campeonato(
            nomeCamp.value!!,
            descricao.value!!,
            tipo.value!!,
            quantidadeParticipantes.value!!,
            auth.currentUser!!.displayName
        )
        if (listParticipantes != null) {
            campeonato.participantes = listParticipantes
        } else {
            campeonato.participantes = participantesList.value!!
        }
        jobs add launch {
            loading.value = true
            try {
                campeonatosRepository.saveCamp(campeonato).await()
                campeonatosRepository.saveCampByUser(campeonato).await()
                campeonatosRepository.saveParticipants(campeonato).await()
                sucess.value = true
            } catch (t: Throwable) {
                error.value = t
            } finally {
                loading.value = false
            }
        }
    }
}