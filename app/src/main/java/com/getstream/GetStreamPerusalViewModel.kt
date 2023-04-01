package com.getstream

import androidx.lifecycle.ViewModel
import com.getstream.data.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GetStreamPerusalViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {


    fun getJwtToken(): Flow<String> = dataStoreRepository.getString("JWT_TOKEN")
    fun getEmailAddress(): Flow<String> = dataStoreRepository.getString("EMAIL")
    fun getDisplayName(): Flow<String> = dataStoreRepository.getString("DISPLAY_NAME")

}