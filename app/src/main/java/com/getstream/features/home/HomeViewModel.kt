package com.getstream.features.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Textsms
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val mockUsers = List(100) {
        User(
            id = "Philip J. Fry $it",
            name = "Philip J. Fry $it",
            image = "https://w7.pngwing.com/pngs/76/870/png-transparent-philip-j-fry-zoidberg-leela-amy-wong-bender-bender-child-face-hat-thumbnail.png",
            online = Random.nextBoolean()
        )
    }

    val users = MutableStateFlow(mockUsers)
    var selectedUsers = mutableStateMapOf<String, Boolean>()

    val channelCreateMode = MutableStateFlow(false)


    fun toggleChannelCreateMode() {
        channelCreateMode.value = !channelCreateMode.value

        if (!channelCreateMode.value) selectedUsers.clear()
    }

    fun setUserSelected(user: User, selected: Boolean) {
        if (selected) {
            selectedUsers[user.id] = true
        } else {
            selectedUsers.remove(user.id)
        }
    }

    fun createChannel() {
    }

    fun startChannelCreateMode() {
        channelCreateMode.value = true
    }

    enum class TopBarState(val imageVector: ImageVector) {
        START_CHAT(Icons.Outlined.Textsms),
        CANCEL_CHAT(Icons.Default.Close),
    }
}