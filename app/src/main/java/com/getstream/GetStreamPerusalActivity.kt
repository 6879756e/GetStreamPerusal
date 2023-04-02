package com.getstream

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.getstream.login.LoginActivity
import com.getstream.navigation.Home
import com.getstream.ui.theme.GetStreamPerusalTheme
import com.getstream.util.toId
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.InitializationState
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@AndroidEntryPoint
class GetStreamPerusalActivity : ComponentActivity() {

    private val onFinishLoadingListener = ViewTreeObserver.OnPreDrawListener { false }

    private val getStreamPerusalViewModel by viewModels<GetStreamPerusalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(onFinishLoadingListener)

        setContent {
            val jwtToken by getStreamPerusalViewModel.getJwtToken().collectAsState(UNINITIALISED)
            val email by getStreamPerusalViewModel.getEmailAddress().collectAsState(initial = "")
            val displayName by getStreamPerusalViewModel.getDisplayName()
                .collectAsState(initial = "")

            val userState by getStreamPerusalViewModel.userState.collectAsState()

            GetStreamPerusalTheme {
                if (userState == InitializationState.NOT_INITIALIZED) {
                    if (jwtToken.isEmpty()) {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        if (!ChatClient.instance().clientState.isInitialized
                            && email.isNotEmpty()
                            && displayName.isNotEmpty()
                        ) {
                            getStreamPerusalViewModel.signIn(
                                User(id = email.toId(), name = displayName), jwtToken
                            )
                        }
                    }
                } else {
                    val navController = rememberNavController()

                    GetStreamNavHost(navController = navController)

                    content.viewTreeObserver.removeOnPreDrawListener(onFinishLoadingListener)
                }
            }
        }
    }

    @Composable
    fun GetStreamNavHost(
        navController: NavHostController,
        modifier: Modifier = Modifier,
    ) {
        NavHost(
            navController = navController,
            startDestination = Home.route,
            modifier = modifier,
        ) {
            composable(route = Home.route) {
                HomeScreen()
            }
        }

    }

    @Composable
    fun HomeScreen() {
        ChatTheme {
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

private const val UNINITIALISED = "uninitialised"