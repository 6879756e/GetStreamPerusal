package com.getstream

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.getstream.ui.theme.GetStreamPerusalTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StartActivity : ComponentActivity() {

    private val onFinishLoadingListener = ViewTreeObserver.OnPreDrawListener { false }

    private val Context.dataStore by preferencesDataStore(USER_PREFERENCES)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(onFinishLoadingListener)

        setContent {
            GetStreamPerusalTheme {
                LaunchedEffect(Unit) {
                    getJwtToken().collect {
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

    private fun getJwtToken(): Flow<String> = dataStore.data.map { preferences ->
        preferences[stringPreferencesKey(JWT_TOKEN_KEY)] ?: ""
    }
}

private const val USER_PREFERENCES = "user_preferences"
private const val JWT_TOKEN_KEY = "jwt_token"