package com.getstream

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.getstream.navigation.Destination
import com.getstream.navigation.Home
import com.getstream.navigation.Login
import com.getstream.ui.login.LoginScreen
import com.getstream.ui.login.signInWithGoogle
import com.getstream.ui.theme.GetStreamPerusalTheme
import com.getstream.viewmodels.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import timber.log.Timber

@AndroidEntryPoint
class GetStreamPerusalActivity : ComponentActivity() {

    private val onFinishLoadingListener = ViewTreeObserver.OnPreDrawListener { false }

    private val getStreamPerusalViewModel by viewModels<GetStreamPerusalViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(onFinishLoadingListener)

        val registerSignInWithGoogle = signInWithGoogle {
            it?.run { loginViewModel.getJwtTokenAndSignIn(this) }
        }

        setContent {
            GetStreamPerusalTheme {
                val jwtToken by getStreamPerusalViewModel.getJwtToken().collectAsState(initial = "")
                val email by getStreamPerusalViewModel.getEmailAddress()
                    .collectAsState(initial = "")
                val displayName by getStreamPerusalViewModel.getDisplayName()
                    .collectAsState(initial = "")

                val navController = rememberNavController()

                val destination = if (
                    jwtToken.isEmpty() ||
                    email.isEmpty() ||
                    displayName.isEmpty()
                ) Login else {
                    ChatClient.instance()
                        .connectUser(
                            user = User(
                                id = email.replace(".", ""),
                                name = displayName,
                                image = "https://bit.ly/2TIt8NR"
                            ), token = jwtToken
                        ).enqueue {
                            Timber.e("enqueue result $it")
                        }

                    Home
                }

                content.viewTreeObserver.removeOnPreDrawListener(
                    onFinishLoadingListener
                )

                GetStreamNavHost(
                    navController = navController,
                    destination = destination,
                    resultLauncher = registerSignInWithGoogle
                )
            }
        }
    }

    private fun getSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this, gso)
    }

    @Composable
    fun GetStreamNavHost(
        navController: NavHostController,
        modifier: Modifier = Modifier,
        destination: Destination = Login,
        resultLauncher: ActivityResultLauncher<GoogleSignInClient>,
    ) {
        NavHost(
            navController = navController,
            startDestination = destination.route,
            modifier = modifier,
        ) {
            composable(route = Login.route) {
                LoginScreen { resultLauncher.launch(getSignInClient()) }
            }
            composable(route = Home.route) {
                HomeScreen()
            }
        }

    }

    @Composable
    fun HomeScreen() {
        ChatTheme {
            Text("HomeScreen")

            ChannelsScreen(
                title = stringResource(id = R.string.app_name),
                isShowingSearch = true,
                onItemClick = { channel ->
                    // TODO Start Messages Activity
                },
            )
        }
    }

}
