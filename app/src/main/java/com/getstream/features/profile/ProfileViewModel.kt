package com.getstream.features.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.getstream.features.USER_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var errorLoading = MutableLiveData(false)

    var user = MutableStateFlow<User?>(null)

    init {
        savedStateHandle.get<String>(USER_ID_KEY)?.run {
            fetchUserDetails(this)
        }
    }

    private fun fetchUserDetails(userId: String) {
        ChatClient.instance().queryUsers(
            QueryUsersRequest(
                filter = Filters.autocomplete("id", userId),
                0, 1
            )
        ).enqueue { result ->
            if (result.isSuccess) {
                user.value = result.data().first()
            } else {
                errorLoading.value = true
            }
        }
    }

}