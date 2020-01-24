package br.com.main.di

import br.com.main.modules.home.repositories.interfaces.UserRepository
import br.com.main.modules.home.repositories.implementation.UserRepositoryImpl
import br.com.main.modules.home.viewmodels.LoginViewModel
import br.com.main.modules.main.repositories.implementation.CampeonatosRepositoryImpl
import br.com.main.modules.main.repositories.interfaces.CampeonatosRepository
import br.com.main.modules.main.viewmodels.CreateCampViewModel
import br.com.main.modules.main.viewmodels.MainViewModel
import br.com.main.modules.main.viewmodels.MeusCampeonatosViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

fun injectFeature() = loadFeature

val loadFeature by lazy {
    loadKoinModules(appModule,
            firebaseModule,
            repositoriesModule)
}

val appModule: Module = module {

    viewModel { LoginViewModel(get()) }
    viewModel { MeusCampeonatosViewModel(get()) }
    viewModel { MainViewModel() }
    viewModel { CreateCampViewModel(get(), get()) }
}

val firebaseModule: Module = module {

    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
}

val repositoriesModule: Module = module {
    single { UserRepositoryImpl(get()) as UserRepository }
    single { CampeonatosRepositoryImpl(get(), get()) as CampeonatosRepository }
}