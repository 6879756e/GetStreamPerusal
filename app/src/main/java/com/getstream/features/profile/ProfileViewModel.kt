package com.getstream.features.profile

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.getstream.features.USER_ID_KEY
import com.getstream.util.getJobDetail
import com.getstream.util.getStatus
import com.getstream.util.setJobDetail
import com.getstream.util.setStatus
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

    val isEditMode = MutableStateFlow(false)

    var errorLoading = MutableLiveData(false)

    var user = MutableStateFlow(User())

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
                resetTextFieldValues()
            } else {
                errorLoading.value = true
            }
        }
    }

    fun toggleEditMode() {
        isEditMode.value = !isEditMode.value
    }

    fun cancelEdit() {
        isEditMode.value = false

        resetTextFieldValues()
    }

    fun updateUser() {
        isEditMode.value = false

        val updatedUser = user.value.copy(name = username.value.text).apply {
            setStatus(status.value.text)
            setJobDetail(job.value.text)
        }

        ChatClient.instance().updateUser(updatedUser).enqueue()
    }

    private fun resetTextFieldValues() {
        username.value = defaultTextFieldValue(user.value.name)
        status.value = defaultTextFieldValue(user.value.getStatus())
        job.value = defaultTextFieldValue(user.value.getJobDetail())
    }

    val username = MutableStateFlow(defaultTextFieldValue(user.value.name))
    val status = MutableStateFlow(defaultTextFieldValue(user.value.getStatus()))
    val job = MutableStateFlow(defaultTextFieldValue(user.value.getJobDetail()))

    private fun defaultTextFieldValue(text: String) =
        TextFieldValue(text = text, selection = TextRange(text.length))

    fun setUserName(username: TextFieldValue) {
        this.username.value = username
    }

    fun setUserStatus(status: TextFieldValue) {
        this.status.value = status
    }

    fun setJobDetail(jobDetail: TextFieldValue) {
        job.value = jobDetail
    }

}