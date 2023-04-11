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
import com.getstream.login.LoginActivity
import com.getstream.navigation.Destination
import com.getstream.navigation.GetStreamPerusalBottomNavigation
import com.getstream.navigation.GetStreamPerusalNavHost
import com.getstream.navigation.Home
import com.getstream.ui.theme.GetStreamPerusalTheme
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.models.InitializationState

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

                if (clientState == InitializationState.COMPLETE) {
                    val navController = rememberNavController()

                    GetStreamPerusalActivityScreen(navController = navController)

                    content.viewTreeObserver.removeOnPreDrawListener(onFinishLoadingListener)
                }
            }
        }
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
        modifier: Modifier = Modifier,
    ) {
        var currentDestination by remember { mutableStateOf<Destination>(Home) }

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            GetStreamPerusalNavHost(navController, modifier.weight(1f), onDestinationChanged = {
                currentDestination = it
            })

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