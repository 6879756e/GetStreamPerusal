package com.getstream

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
                LaunchedEffect(Unit) {
                    viewModel.jwtToken.collect {
                        // content.viewTreeObserver.removeOnPreDrawListener(onFinishLoadingListener) TODO: Uncomment once flow is decided
                        if (it.isEmpty()) {
                            // TODO: Go to login screen
                        } else {
                            // TODO: Check jwt is valid, then log in if valid
                        }
                    }
                }
            }
        }
    }
}
