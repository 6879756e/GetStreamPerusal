package com.getstream.data

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun putString(key: String, value: String)

    fun getString(key: String): Flow<String>

    suspend fun setUserJwtToken(token: String)
    fun getUserJwtToken(): Flow<String>

    suspend fun setUserEmail(email: String)
    fun getUserEmail(): Flow<String>

    suspend fun setUserDisplayName(displayName: String)
    fun getUserDisplayName(): Flow<String>

    suspend fun clearUserData()

}