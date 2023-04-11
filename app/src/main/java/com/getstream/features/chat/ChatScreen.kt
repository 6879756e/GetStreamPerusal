package com.getstream.features.chat

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.querysort.QuerySortByField
import io.getstream.chat.android.compose.ui.channels.list.ChannelList
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory

@Composable
fun ChatScreen(
    cid: String? = null
) {
    ChatTheme {
        if (cid == null) {
            ChannelList(
                viewModel = viewModel(
                    factory =
                    ChannelViewModelFactory(
                        ChatClient.instance(),
                        QuerySortByField.descByName("last_updated"),
                        null
//                        filters = Filters.contains(member) TODO: Filter for channels that user is included in
                    )
                )
            )
        } else {
            MessagesScreen(channelId = cid)
        }
    }
}