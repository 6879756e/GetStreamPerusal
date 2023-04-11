package com.getstream.features.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Textsms
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.NeutralFilterObject
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.events.UserPresenceChangedEvent
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.subscribeFor
import io.getstream.chat.android.client.utils.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    var users = mutableStateListOf<User>()
    var selectedUsers = mutableStateMapOf<String, User>()

    val channelCreateMode = MutableStateFlow(false)

    val isChannelBeingCreated = MutableStateFlow(false)

    init {
        fetchAllUsers()
    }

    private fun fetchAllUsers() {
        @Suppress("LocalVariableName") val _users = mutableListOf<User>()

        fetchAllUsers(_users, onAllUsersFetched = {
            _users.sortWith(userComparator())
            users.addAll(_users)

            ChatClient.instance().subscribeFor<UserPresenceChangedEvent> { event ->
                users.find { event.user.id == it.id }?.run {
                    users.remove(this)
                    users.add(event.user)
                    users.sortWith(userComparator())
                }
            }
        })
    }

    private fun userComparator(): Comparator<User> = compareBy({ !it.online }, { it.name })

    private var offset = 0
    private fun fetchAllUsers(_users: MutableList<User>, onAllUsersFetched: () -> Unit) {
        ChatClient.instance().queryUsers(
            QueryUsersRequest(
                filter = NeutralFilterObject,
                offset = offset,
                limit = DEFAULT_LIMIT,
                presence = true
            )
        ).enqueue { result ->
            result.onSuccess { users ->
                _users.addAll(users)
                if (users.size == DEFAULT_LIMIT) {
                    offset += DEFAULT_LIMIT
                    fetchAllUsers(_users, onAllUsersFetched)
                } else {
                    onAllUsersFetched()
                }
            }
        }
    }

    fun toggleChannelCreateMode() {
        channelCreateMode.value = !channelCreateMode.value

        if (!channelCreateMode.value) selectedUsers.clear()
    }

    fun setUserSelected(user: User, selected: Boolean) {
        if (selected) {
            selectedUsers[user.id] = user
        } else {
            selectedUsers.remove(user.id)
        }
    }

    fun startChannelCreateMode() {
        channelCreateMode.value = true
    }

    fun createChannel(onSuccess: (Channel) -> Unit) {
        isChannelBeingCreated.value = true

        val memberIds = selectedUsers.keys.toList() + ChatClient.instance().getCurrentUser()!!.id

        ChatClient.instance().createChannel(
            channelType = CHANNEL_TYPE_MESSAGING,
            channelId = "",
            memberIds = memberIds,
            extraData = emptyMap()
        ).enqueue { result ->
            isChannelBeingCreated.value = false
            result.onSuccess { channel ->
                onSuccess(channel)
            }
        }
    }

    enum class TopBarState(val imageVector: ImageVector) {
        START_CHAT(Icons.Outlined.Textsms), CANCEL_CHAT(Icons.Default.Close),
    }
}

private const val DEFAULT_LIMIT = 30
private const val CHANNEL_TYPE_MESSAGING = "messaging"