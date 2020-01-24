package br.com.main.modules.home.repositories.implementation

import br.com.main.modules.home.repositories.interfaces.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class UserRepositoryImpl(val mAuth: FirebaseAuth) : UserRepository {

    override fun updateUserName(name: String) {
        val profileUpdate = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        mAuth!!.currentUser!!.updateProfile(profileUpdate)
    }
}