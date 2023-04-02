package com.getstream

import androidx.lifecycle.ViewModel
import com.getstream.data.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.ConnectionData
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GetStreamPerusalViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val userState = ChatClient.instance().clientState.initializationState


    fun getJwtToken(): Flow<String> = dataStoreRepository.getString("JWT_TOKEN")
    fun getEmailAddress(): Flow<String> = dataStoreRepository.getString("EMAIL")
    fun getDisplayName(): Flow<String> = dataStoreRepository.getString("DISPLAY_NAME")

    fun signIn(user: User, token: String, callback: (Result<ConnectionData>) -> Unit = {}) =
        connectUser(user, token, callback)
}