package br.com.main.modules.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.commons.SampleNavigation
import br.com.commons.hideKeyboard
import br.com.main.R
import br.com.main.modules.home.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModel()
    private val auth: FirebaseAuth by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        btn_login.background = resources.getDrawable(R.drawable.background_button_login)

        back_button.setOnClickListener {
            btn_login.hideKeyboard()
            findNavController().navigateUp()
        }

        btn_login.setOnClickListener {
            btn_login.hideKeyboard()
            btn_login.startMorphAnimation()
            logar()
        }

        txt_help_login.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_esqueciSenhaFragment)
        }

        edt_email.doOnTextChanged { text, _, _, _ -> viewModel.email.value = text.toString() }
        edt_senha.doOnTextChanged { text, _, _, _ -> viewModel.senha.value = text.toString() }

        viewModel.enableToLogin.observe(this, Observer {
            btn_login.isEnabled = it
        })
    }

    fun logar() {
        auth.signInWithEmailAndPassword(viewModel.email.value!!, viewModel.senha.value!!)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    startActivity(SampleNavigation.navigateActivityClearTask(SampleNavigation.MAIN_ACTIVITY))
                } else {
                    btn_login.startMorphRevertAnimation()
                    if (task.exception!!.message!!.contains("password")) {
                        Snackbar.make(
                            btn_login,
                            R.string.erro_login_password_user,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        Snackbar.make(btn_login, R.string.erro_login, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }
}