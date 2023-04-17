package com.getstream.features.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstream.data.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.utils.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val user = MutableStateFlow(ChatClient.instance().getCurrentUser()!!)
    val isEditStatusMode = MutableStateFlow(false)

    fun toggleOnlineStatus() {
        viewModelScope.launch {
            val token = dataStoreRepository.getUserJwtToken().first()
            val currentUser = user.value

            toggleOnlineStatus(currentUser, token)
        }
    }

    private fun toggleOnlineStatus(currentUser: User, token: String) {
        ChatClient.instance().disconnect(false).enqueue {
            ChatClient.instance()
                .connectUser(currentUser.copy(invisible = !currentUser.invisible), token)
                .enqueue { result ->
                    if (result.isSuccess) {
                        result.onSuccess { user.value = it.user }
                    }
                }
        }
    }
}