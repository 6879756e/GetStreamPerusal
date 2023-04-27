package com.getstream

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.getstream.features.USER_ID_KEY
import com.getstream.features.chat.ChannelRoomState
import com.getstream.features.profile.ProfileActivity
import com.getstream.login.LoginActivity
import com.getstream.navigation.Destination
import com.getstream.navigation.GetStreamPerusalBottomNavigation
import com.getstream.navigation.GetStreamPerusalNavHost
import com.getstream.navigation.Home
import com.getstream.ui.theme.GetStreamPerusalTheme
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.models.InitializationState
import io.getstream.chat.android.client.models.User

@AndroidEntryPoint
class GetStreamPerusalActivity : ComponentActivity() {

    private val onFinishLoadingListener = ViewTreeObserver.OnPreDrawListener { false }

    private val getStreamPerusalViewModel by viewModels<GetStreamPerusalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(onFinishLoadingListener)

        observeIsLoginRequired()

        setContent {
            GetStreamPerusalTheme {
                val clientState by getStreamPerusalViewModel.clientState.collectAsStateWithLifecycle()
                val fullScreenMode by getStreamPerusalViewModel.fullScreenMode.collectAsStateWithLifecycle()

                if (clientState == InitializationState.COMPLETE) {
                    val navController = rememberNavController()

                    GetStreamPerusalActivityScreen(
                        navController = navController,
                        fullScreenMode = fullScreenMode,
                        onUserClicked = { startDetailedProfileActivity(it) },
                        onChannelRoomStateChanged = { channelRoomState ->
                            getStreamPerusalViewModel.setFullScreenMode(channelRoomState is ChannelRoomState.Entered)
                        }
                    )

                    content.viewTreeObserver.removeOnPreDrawListener(onFinishLoadingListener)
                }
            }
        }
    }

    private fun startDetailedProfileActivity(user: User) {
        startActivity(
            Intent(this, ProfileActivity::class.java).apply {
                putExtra(USER_ID_KEY, user.id)
            }
        )
    }

    private fun observeIsLoginRequired() =
        getStreamPerusalViewModel.isLoginRequired().observe(this) { loginRequired ->
            if (loginRequired) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

    @Composable
    fun GetStreamPerusalActivityScreen(
        navController: NavHostController,
        fullScreenMode: Boolean,
        modifier: Modifier = Modifier,
        onChannelRoomStateChanged: (ChannelRoomState) -> Unit,
        onUserClicked: (User) -> Unit,
    ) {
        var currentDestination by remember { mutableStateOf<Destination>(Home) }

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            GetStreamPerusalNavHost(
                navController, modifier.weight(1f),
                onDestinationChanged = {
                    currentDestination = it
                },
                onUserClicked = onUserClicked,
                onChannelRoomStateChanged = onChannelRoomStateChanged
            )

            if (!fullScreenMode) {
                GetStreamPerusalBottomNavigation(
                    currentDestination = currentDestination,
                    onItemClicked = {
                        currentDestination = it
                        navController.navigate(it.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}