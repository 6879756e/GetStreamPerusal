package com.getstream

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    context: Application
) : ViewModel() {

    val jwtToken: Flow<String> = context.applicationContext.dataStore.data.map { preferences ->
        preferences[stringPreferencesKey(JWT_TOKEN_KEY)] ?: ""
    }

}

private const val JWT_TOKEN_KEY = "jwt_token"
private const val USER_PREFERENCES = "user_preferences"
val Context.dataStore by preferencesDataStore(USER_PREFERENCES)