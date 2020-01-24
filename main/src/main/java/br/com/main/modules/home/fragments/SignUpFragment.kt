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
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModel()
    private val auth: FirebaseAuth by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sign_up, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        btn_cadastrar.background = resources.getDrawable(R.drawable.background_button_login)

        back_button.setOnClickListener {
            findNavController().navigateUp()
        }

        btn_cadastrar.setOnClickListener {
            btn_cadastrar.hideKeyboard()
            btn_cadastrar.startMorphAnimation()
            cadastrar()
        }

        edt_nome.doOnTextChanged { text, _, _, _ -> viewModel.nomeSignUp.value = text.toString() }
        edt_email.doOnTextChanged { text, _, _, _ -> viewModel.emailSignUp.value = text.toString() }
        edt_senha.doOnTextChanged { text, _, _, _ -> viewModel.senhaSignUp.value = text.toString() }

        viewModel.enableToSignUp.observe(this, Observer {
            btn_cadastrar.isEnabled = it
        })
    }

    fun cadastrar() {
        auth.createUserWithEmailAndPassword(
            viewModel.emailSignUp.value!!,
            viewModel.senhaSignUp.value!!
        ).addOnCompleteListener(activity!!) { task ->
            if (task.isSuccessful) {
                viewModel.atualizarNomeUsuario(viewModel.nomeSignUp.value!!)
                startActivity(SampleNavigation.navigateActivityClearTask(SampleNavigation.MAIN_ACTIVITY))
            } else {
                btn_cadastrar.startMorphRevertAnimation()
                Snackbar.make(btn_cadastrar, R.string.erro_cadastro, Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }
}