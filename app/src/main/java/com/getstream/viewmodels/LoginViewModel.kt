package com.getstream.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstream.EmailSignUpRequest
import com.getstream.data.DataStoreRepository
import com.getstream.signUpEmailApi
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    fun getJwtTokenAndSignIn(googleAccount: GoogleSignInAccount) {
        viewModelScope.launch {
            val formattedEmail = googleAccount.email!!.replace(".", "")
            val request = EmailSignUpRequest(formattedEmail)
            signUpEmailApi.signUpEmailAddress(request)
                .runCatching {
                    if (this.isSuccessful) {
                        body()?.jwtToken?.let { token ->
                            dataStoreRepository.putString("JWT_TOKEN", token)
                            dataStoreRepository.putString("EMAIL", formattedEmail)
                            dataStoreRepository.putString(
                                "DISPLAY_NAME",
                                googleAccount.displayName!!
                            )

                            signInOnStream(
                                User(
                                    id = formattedEmail,
                                    name = googleAccount.displayName!!,
                                    image = "https://bit.ly/2TIt8NR"
                                ), token
                            )
                        }
                    }
                }
        }
    }

    private fun signInOnStream(user: User, jwtToken: String) =
        ChatClient.instance().connectUser(user = user, token = jwtToken).enqueue()

}