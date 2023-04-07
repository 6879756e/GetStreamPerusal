package com.getstream

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.getstream.login.LoginActivity
import com.getstream.navigation.Chat
import com.getstream.navigation.Destination
import com.getstream.navigation.Home
import com.getstream.navigation.More
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

        Column {
            GetStreamPerusalNavHost(navController, modifier.weight(1f))

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

    @Composable
    private fun GetStreamPerusalNavHost(
        navController: NavHostController, modifier: Modifier
    ) {
        NavHost(
            navController = navController,
            startDestination = Home.route,
            modifier = modifier,
        ) {
            composable(route = Home.route) {
                Box(modifier = Modifier.fillMaxSize(1f)) {
                    Text(text = Home.label, modifier = Modifier.align(Alignment.Center))
                }
            }
            composable(route = Chat.route) {
                Box(modifier = Modifier.fillMaxSize(1f)) {
                    Text(text = Chat.label, modifier = Modifier.align(Alignment.Center))
                }
            }
            composable(route = More.route) {
                Box(modifier = Modifier.fillMaxSize(1f)) {
                    Text(text = More.label, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }

    @Composable
    private fun GetStreamPerusalBottomNavigation(
        currentDestination: Destination,
        onItemClicked: (Destination) -> Unit,
    ) {
        BottomNavigation {
            Destination.allDestinations.forEach {
                BottomNavigationItem(
                    selected = it == currentDestination,
                    onClick = { onItemClicked(it) },
                    icon = {
                        Icon(
                            imageVector = it.imageVector,
                            contentDescription = "Decorative icon for ${it.label}"
                        )
                    },
                    label = {
                        Text(text = it.label, style = MaterialTheme.typography.labelSmall)
                    }
                )
            }
        }
    }
}