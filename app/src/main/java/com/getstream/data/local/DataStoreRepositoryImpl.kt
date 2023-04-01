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
}


private const val USER_PREFERENCES = "user_preferences"
private val Context.dataStore by preferencesDataStore(USER_PREFERENCES)