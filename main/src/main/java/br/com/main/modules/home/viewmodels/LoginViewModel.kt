package br.com.main.modules.home.viewmodels

import androidx.lifecycle.*
import br.com.commons.isValidEmail
import br.com.commons.isValidPassword
import br.com.main.modules.home.repositories.interfaces.UserRepository
import br.com.utils.DoubleMediatorLiveData
import br.com.utils.TripleMediatorLiveData
import com.google.common.base.Strings

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    var email: MutableLiveData<String> = MutableLiveData()
    var senha: MutableLiveData<String> = MutableLiveData()
    var enableToLogin: LiveData<Boolean> = MutableLiveData()

    var enableToRecover: MediatorLiveData<Boolean> = MediatorLiveData()

    var nomeSignUp: MutableLiveData<String> = MutableLiveData()
    var emailSignUp: MutableLiveData<String> = MutableLiveData()
    var senhaSignUp: MutableLiveData<String> = MutableLiveData()
    var enableToSignUp: LiveData<Boolean> = MutableLiveData()

    init {
        enableToLogin = Transformations.switchMap(DoubleMediatorLiveData(email, senha)) {
            logar(it.first, it.second)
        }

        enableToSignUp = Transformations.switchMap(
            TripleMediatorLiveData(
                nomeSignUp,
                emailSignUp,
                senhaSignUp
            )
        ) {
            cadastrar(it.first, it.second, it.third)
        }
    }

    fun atualizarNomeUsuario(name: String) {
        userRepository.updateUserName(name)
    }

    fun cadastrar(nome: String?, email: String?, senha: String?): LiveData<Boolean> {
        val retorno: MutableLiveData<Boolean> = MutableLiveData()
        retorno.value =
            email.isValidEmail() && senha.isValidPassword() && !Strings.isNullOrEmpty(nome)
        return retorno
    }

    fun logar(email: String?, senha: String?): LiveData<Boolean> {
        val retorno: MutableLiveData<Boolean> = MutableLiveData()
        retorno.value = email.isValidEmail() && !Strings.isNullOrEmpty(senha)
        return retorno
    }
}