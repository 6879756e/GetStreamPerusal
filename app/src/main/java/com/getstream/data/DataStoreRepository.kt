package com.getstream.data

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun putString(key: String, value: String)

    fun getString(key: String): Flow<String>
}