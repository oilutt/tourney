package br.com.main.modules.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.commons.hideKeyboard
import br.com.commons.isValidEmail
import br.com.main.R
import br.com.main.modules.home.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_esqueci_senha.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EsqueciSenhaFragment : Fragment() {


    private val viewModel: LoginViewModel by viewModel()
    private val auth: FirebaseAuth by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_esqueci_senha, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_enviar.background = resources.getDrawable(R.drawable.background_button_login)

        back_button.setOnClickListener {
            findNavController().navigateUp()
        }

        btn_enviar.setOnClickListener {
            btn_enviar.hideKeyboard()
            btn_enviar.startMorphAnimation()
            recuperarConta()
        }

        edt_email.doOnTextChanged { text, _, _, _ ->
            viewModel.enableToRecover.value = text.toString().isValidEmail()
        }

        viewModel.enableToRecover.observe(this, Observer {
            btn_enviar.isEnabled = it
        })
    }

    fun recuperarConta() {
        auth.sendPasswordResetEmail(edt_email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Snackbar.make(
                        btn_enviar,
                        R.string.email_enviado_sucesso,
                        Snackbar.LENGTH_SHORT
                    )
                    findNavController().popBackStack(R.id.homeFragment, false)
                } else {
                    btn_enviar.startMorphRevertAnimation()
                    Snackbar.make(btn_enviar, R.string.erro, Snackbar.LENGTH_SHORT)
                }
            }
    }
}
