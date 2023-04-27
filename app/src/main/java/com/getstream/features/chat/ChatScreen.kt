package com.getstream.features.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.getstream.ui.core.TopBar
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.querysort.QuerySortByField
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.compose.ui.channels.list.ChannelList
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory

@Composable
fun ChatScreen(
    channelId: String? = null,
    onChannelRoomStateChanged: (ChannelRoomState) -> Unit,
    onBackPressed: () -> Unit,
) {
    ChatTheme {
        Surface {
            Column(modifier = Modifier.fillMaxSize()) {
                channelId?.let {
                    MessagesScreen(
                        channelId = it,
                        onBackPressed = {
                            onChannelRoomStateChanged(ChannelRoomState.Left)
                        }
                    )
                } ?: ChannelsScreen(
                    onChannelClick = {
                        onChannelRoomStateChanged(ChannelRoomState.Entered(it.cid))
                    }, onBackPressed = onBackPressed
                )
            }
        }
    }
}

@Composable
private fun ChannelsScreen(
    onChannelClick: (Channel) -> Unit = {},
    onBackPressed: () -> Unit,
) {
    TopBar(title = "Channels")

    ChannelList(
        modifier = Modifier.fillMaxSize(),
        viewModel = viewModel(
            factory =
            ChannelViewModelFactory(
                ChatClient.instance(),
                QuerySortByField.descByName("last_updated"),
                null
            )
        ),
        onChannelClick = onChannelClick
    )

    BackHandler(true) {
        onBackPressed()
    }
}


sealed class ChannelRoomState(var cid: String?) {
    class Entered(cid: String) : ChannelRoomState(cid)
    object Left : ChannelRoomState(null)

}