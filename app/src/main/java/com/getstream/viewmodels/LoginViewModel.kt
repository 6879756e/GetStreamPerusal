package com.getstream.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstream.EmailSignUpRequest
import com.getstream.connectUser
import com.getstream.data.DataStoreRepository
import com.getstream.signUpEmailApi
import com.getstream.util.toId
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.ConnectionData
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.utils.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    var clientState = ChatClient.instance().clientState.initializationState

    fun getJwtTokenAndSignIn(googleAccount: GoogleSignInAccount) {
        viewModelScope.launch {
            val id = googleAccount.email!!.toId()
            val request = EmailSignUpRequest(id)

            signUpEmailApi.signUpEmailAddress(request)
                .runCatching {
                    if (this.isSuccessful) {
                        body()?.jwtToken?.let { token ->
                            dataStoreRepository.putString("JWT_TOKEN", token)
                            dataStoreRepository.putString("EMAIL", id)
                            dataStoreRepository.putString(
                                "DISPLAY_NAME",
                                googleAccount.displayName!!
                            )

                            signIn(
                                User(
                                    id = id,
                                    name = googleAccount.displayName!!,
                                    image = "https://bit.ly/2TIt8NR"
                                ), token
                            )
                        }
                    }
                }
        }
    }

    private fun signIn(user: User, token: String, callback: (Result<ConnectionData>) -> Unit = {}) =
        connectUser(user, token, callback)

}