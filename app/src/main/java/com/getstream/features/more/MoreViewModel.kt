package com.getstream.features.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstream.data.DataStoreRepository
import com.getstream.util.getStatus
import com.getstream.util.statusKey
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

    fun toggleEditStatusMode() {
        isEditStatusMode.value = !isEditStatusMode.value
    }

    fun setStatus(status: String) {
        if (user.value.getStatus() != status) {
            ChatClient.instance()
                .partialUpdateUser(user.value.id, mapOf(user.value.statusKey to status))
                .enqueue { result ->
                    if (result.isSuccess) {
                        result.onSuccess { updatedUser -> user.value = updatedUser }
                    }
                }
        }
        isEditStatusMode.value = false
    }
}