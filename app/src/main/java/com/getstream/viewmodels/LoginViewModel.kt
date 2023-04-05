package com.getstream.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.getstream.EmailSignUpRequest
import com.getstream.connectUser
import com.getstream.data.DataStoreRepository
import com.getstream.signUpEmailApi
import com.getstream.util.toId
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    var clientState = ChatClient.instance().clientState.initializationState.asLiveData()

    val isConnecting = MutableStateFlow(false)

    fun getJwtTokenAndSignIn(googleAccount: GoogleSignInAccount) {
        viewModelScope.launch {
            isConnecting.value = true

            val id = googleAccount.email!!.toId()
            val request = EmailSignUpRequest(id)

            signUpEmailApi.signUpEmailAddress(request).runCatching {
                if (this.isSuccessful) {
                    body()?.jwtToken?.let { token ->
                        val displayName = googleAccount.displayName!!
                        storeUserDetails(token, id, displayName)

                        connectUser(
                            User(
                                id = id,
                                name = displayName,
                                image = "https://bit.ly/2TIt8NR"
                            ), token
                        ) {
                            isConnecting.value = false
                        }
                    }
                }
            }
        }
    }

    private suspend fun storeUserDetails(
        token: String, id: String, displayName: String
    ) = dataStoreRepository.run {
        setUserJwtToken(token)
        setUserEmail(id)
        setUserDisplayName(displayName)
    }
}