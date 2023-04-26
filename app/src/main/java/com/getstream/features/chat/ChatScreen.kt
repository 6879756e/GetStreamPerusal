package com.getstream.features.chat

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.querysort.QuerySortByField
import io.getstream.chat.android.compose.ui.channels.list.ChannelList
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory

@Composable
fun ChatScreen(
    cid: String? = null,
    chatViewModel: ChatViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    chatViewModel.setChannelId(cid)
    val channelId by chatViewModel.channelId.collectAsStateWithLifecycle()

    ChatTheme {
        channelId?.let {
            MessagesScreen(
                channelId = it,
                onBackPressed = {
                    chatViewModel.setChannelId(null)
                }
            )
        } ?: ChannelList(
            viewModel = viewModel(
                factory =
                ChannelViewModelFactory(
                    ChatClient.instance(),
                    QuerySortByField.descByName("last_updated"),
                    null
                )
            ),
            onChannelClick = {
                chatViewModel.setChannelId(it.cid)
            },
        )

        BackHandler(true) {
            if (channelId == null) {
                onBackPressed()
            } else {
                chatViewModel.setChannelId(null)
            }
        }
    }
}