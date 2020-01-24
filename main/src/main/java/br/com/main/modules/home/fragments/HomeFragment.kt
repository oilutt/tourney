package br.com.main.modules.home.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.commons.SampleNavigation
import br.com.commons.SampleNavigation.MAIN_ACTIVITY
import br.com.main.R
import br.com.main.modules.home.viewmodels.LoginViewModel
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private val RC_SIGN_IN: Int = 1
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private var callbackManager: CallbackManager = CallbackManager.Factory.create()

    private val viewModel: LoginViewModel by viewModel()
    private val auth: FirebaseAuth by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        try {
//            val info = context!!.packageManager.getPackageInfo(
//                    "br.com.oilutt.tournamentmanager",
//                    PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//
//        } catch (e: NoSuchAlgorithmException) {
//
//        }

        btn_facebook.background = resources.getDrawable(R.drawable.background_button_facebook)
        btn_google.background = resources.getDrawable(R.drawable.background_button_google)

        configureGoogleSignIn()
        configureFacebook()

        btn_cadastrar.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_signUpFragment)
        }

        btn_google.setOnClickListener {
            btn_google.startMorphAnimation()
            startActivityForResult(mGoogleSignInClient.signInIntent, RC_SIGN_IN)
        }

        btn_facebook.setOnClickListener {
            btn_facebook.startMorphAnimation()
            LoginManager.getInstance()
                .logInWithReadPermissions(activity!!, listOf("public_profile", "email"))
        }

        btn_login.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.e("ERRO GOOGLE", e.message)
                btn_google.startMorphRevertAnimation()
                Snackbar.make(btn_google, R.string.erro, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(br.com.tourney.R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), mGoogleSignInOptions)
    }

    private fun configureFacebook() {
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    btn_facebook.startMorphRevertAnimation()
                }

                override fun onError(error: FacebookException) {
                    Log.e("ERRO FACEBOOK", error?.message)
                    btn_facebook.startMorphRevertAnimation()
                }
            })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnSuccessListener {
            saveUserFacebook(token)
            startActivity(SampleNavigation.navigateActivityClearTask(MAIN_ACTIVITY))
        }.addOnFailureListener {
            Log.e("FAILUER FACEBOOK", it?.message)
            Snackbar.make(btn_facebook, R.string.erro, Snackbar.LENGTH_SHORT).show()
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut()
            }
        }.addOnCompleteListener {
            if (!it.isSuccessful) {
                btn_google.startMorphRevertAnimation()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                viewModel.atualizarNomeUsuario(account.displayName!!)
                startActivity(SampleNavigation.navigateActivityClearTask(MAIN_ACTIVITY))
            }.addOnFailureListener {
                Log.e("FAILURE GOOGLE", it.message)
                Snackbar.make(btn_google, R.string.erro, Snackbar.LENGTH_SHORT).show()
            }.addOnCompleteListener {
                if (!it.isSuccessful) {
                    btn_google.startMorphRevertAnimation()
                }
            }
    }

    private fun saveUserFacebook(token: AccessToken) {
        val request = GraphRequest.newMeRequest(token) { user, _ ->
            run {
                viewModel.atualizarNomeUsuario(user.getString("name"))
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "name,email,id,picture.type(large)")
        request.parameters = parameters
        request.executeAsync()
    }
}
