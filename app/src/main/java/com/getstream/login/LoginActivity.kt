package com.getstream.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.getstream.GetStreamPerusalActivity
import com.getstream.ui.login.LoginScreen
import com.getstream.ui.login.SignInOption
import com.getstream.ui.login.signInWithGoogle
import com.getstream.ui.theme.GetStreamPerusalTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.models.InitializationState

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val resultLauncher = signInWithGoogle {
            it?.run { loginViewModel.getJwtTokenAndSignIn(this) }
        }

        observeClientState()

        setContent {
            GetStreamPerusalTheme {
                LoginScreen(
                    onSignInOptionClicked = {
                        if (it == SignInOption.GOOGLE) {
                            resultLauncher.launch(getSignInClient())
                        }
                    },
                    onBackPressed = { finish() }
                )
            }
        }
    }

    private fun observeClientState() = loginViewModel.clientState.observe(this) { state ->
        if (state == InitializationState.COMPLETE) {
            finish()
            startActivity(Intent(this, GetStreamPerusalActivity::class.java))
        }
    }


    private fun getSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this, gso)
    }
}