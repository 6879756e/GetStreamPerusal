package com.getstream

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.getstream.navigation.Destination
import com.getstream.navigation.Login
import com.getstream.ui.login.LoginScreen
import com.getstream.ui.theme.GetStreamPerusalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : ComponentActivity() {

    private val onFinishLoadingListener = ViewTreeObserver.OnPreDrawListener { false }

    private val viewModel by viewModels<StartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(onFinishLoadingListener)

        setContent {
            GetStreamPerusalTheme {
                val navController = rememberNavController()
                LaunchedEffect(Unit) {
                    viewModel.jwtToken.collect {
                        // content.viewTreeObserver.removeOnPreDrawListener(onFinishLoadingListener) TODO: Uncomment once flow is decided
                        if (it.isEmpty()) {
                            setContent {
                                content.viewTreeObserver.removeOnPreDrawListener(onFinishLoadingListener)
                                GetStreamNavHost(navController = navController)
                            }
                        } else {
                            // TODO: Check jwt is valid, then log in if valid
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GetStreamNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    destination: Destination = Login,
) {
    NavHost(
        navController = navController,
        startDestination = destination.route,
        modifier = modifier,
    ) {
        composable(route = Login.route) {
            LoginScreen()
        }
    }

}
