package br.com.main.modules.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.commons.CoroutineViewModel
import br.com.main.modules.main.model.Campeonato
import br.com.main.modules.main.repositories.interfaces.CampeonatosRepository
import kotlinx.coroutines.launch

class MeusCampeonatosViewModel(private val campeonatosRepository: CampeonatosRepository) :
    CoroutineViewModel() {

    private val campeonatoList: MutableLiveData<List<Campeonato>> = MutableLiveData()
    fun campeonatos() = campeonatoList as LiveData<List<Campeonato>>

    private val loading: MutableLiveData<Boolean> = MutableLiveData()
    fun loading() = loading as LiveData<Boolean>

    private val error: MutableLiveData<Throwable> = MutableLiveData()
    fun error() = error as LiveData<Throwable>

    fun buscarMeusCampeonatos() {
        jobs add launch {
            try {
                loading.value = true
                campeonatoList.value =
                    campeonatosRepository.getMyCamps().await().toObjects(Campeonato::class.java)
            } catch (e: Exception) {
                error.value = e
            } finally {
                loading.value = false
            }
        }
    }
}