package com.getstream

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.getstream.data.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.InitializationState
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetStreamPerusalViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val isLoginRequired = MutableStateFlow(false)

    val clientState = MutableStateFlow(InitializationState.NOT_INITIALIZED)
    val fullScreenMode = MutableStateFlow(false)

    private val jwtToken: Flow<String> = dataStoreRepository.getUserJwtToken()
    private val emailAddress: Flow<String> = dataStoreRepository.getUserEmail()
    private val displayName: Flow<String> = dataStoreRepository.getUserDisplayName()

    init {
        signInIfUserDataPresent()
        updateClientStateOnFirstConnection()
    }

    private fun signInIfUserDataPresent() = viewModelScope.launch {
        combine(jwtToken, emailAddress, displayName) { token, email, name ->
            if (token.isEmpty()) return@combine null

            User(id = email, name = name) to token
        }.collect { userInfo ->
            if (userInfo == null) {
                isLoginRequired.value = true
            } else if (ChatClient.instance().getCurrentUser() == null) {
                attemptToSignIn(userInfo.first, userInfo.second)
            }
        }
    }

    private fun attemptToSignIn(user: User, token: String) = connectUser(user, token) { result ->
        if (result.isError) {
            viewModelScope.launch { dataStoreRepository.clearUserData() }
        }
    }

    private fun updateClientStateOnFirstConnection() = viewModelScope.launch {
        clientState.value = ChatClient.instance().clientState.initializationState.first {
            it == InitializationState.COMPLETE
        }
    }

    fun isLoginRequired(): LiveData<Boolean> = isLoginRequired.asLiveData()

    fun setFullScreenMode(isFullScreenMode: Boolean) {
        fullScreenMode.value = isFullScreenMode
    }

}