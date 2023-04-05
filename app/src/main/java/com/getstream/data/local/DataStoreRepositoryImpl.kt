package com.getstream.data.local

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.getstream.data.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(
    private val context: Application
) : DataStoreRepository {

    override suspend fun putString(key: String, value: String) {
        val preferenceKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }

    override fun getString(key: String): Flow<String> {
        val preferenceKey = stringPreferencesKey(key)
        return context.dataStore.data.map { it[preferenceKey] ?: "" }
    }

    override suspend fun setUserJwtToken(token: String) = putString(userJwtToken, token)
    override fun getUserJwtToken() = getString(userJwtToken)

    override suspend fun setUserEmail(email: String) = putString(userEmail, email)
    override fun getUserEmail() = getString(userEmail)

    override suspend fun setUserDisplayName(displayName: String) =
        putString(userDisplayName, displayName)

    override fun getUserDisplayName() = getString(userDisplayName)

    override suspend fun clearUserData() {
        val userDataKeys = listOf(userJwtToken, userEmail, userDisplayName)

        userDataKeys.forEach {
            context.dataStore.edit { preferences ->
                preferences.remove(stringPreferencesKey(it))
            }
        }
    }

    companion object {
        private const val userJwtToken = "USER_JWT_TOKEN"
        private const val userEmail = "USER_EMAIL"
        private const val userDisplayName = "USER_DISPLAY_NAME"
    }
}


private const val USER_PREFERENCES = "user_preferences"
private val Context.dataStore by preferencesDataStore(USER_PREFERENCES)