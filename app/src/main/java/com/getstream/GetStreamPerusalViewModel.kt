package com.getstream

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.getstream.data.DataStoreRepository
import com.getstream.util.toId
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetStreamPerusalViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val isLoginRequired = MutableStateFlow(false)

    val isUserAuthenticated = MutableStateFlow(false)

    private val jwtToken: Flow<String> = dataStoreRepository.getUserJwtToken()
    private val emailAddress: Flow<String> = dataStoreRepository.getUserEmail()
    private val displayName: Flow<String> = dataStoreRepository.getUserDisplayName()

    init {
        viewModelScope.launch {
            jwtToken.collect { token ->
                if (token.isEmpty()) {
                    isLoginRequired.value = true
                } else if (ChatClient.instance().getCurrentUser() == null) {
                    attemptToSignIn(token)
                }
            }
        }
    }

    private suspend fun attemptToSignIn(token: String) =
        combine(emailAddress, displayName) { email, displayName ->
            User(email.toId(), displayName)
        }.collect { user ->
            connectUser(user, token) {
                if (it.isSuccess) {
                    isUserAuthenticated.value = true
                } else {
                    viewModelScope.launch {
                        dataStoreRepository.clearUserData()
                    }
                }
            }
        }


    fun isLoginRequired(): LiveData<Boolean> = isLoginRequired.asLiveData()

}