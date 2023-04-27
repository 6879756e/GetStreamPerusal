package com.getstream.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.getstream.features.chat.ChannelRoomState
import com.getstream.features.chat.ChatScreen
import com.getstream.features.chat.ChatViewModel
import com.getstream.features.home.HomeScreen
import com.getstream.features.more.MoreScreen
import io.getstream.chat.android.client.models.User

@Composable
fun GetStreamPerusalNavHost(
    navController: NavHostController,
    modifier: Modifier,
    onDestinationChanged: (Destination) -> Unit,
    onUserClicked: (User) -> Unit,
    onChannelRoomStateChanged: (ChannelRoomState) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier,
    ) {
        composable(route = Home.route) {
            HomeScreen(
                onChannelCreated = {
                    navController.navigate(Chat.channel(it.cid))
                    onDestinationChanged(Chat)
                },
                onUserClicked = { onUserClicked(it) }
            )
        }
        composable(
            route = Chat.route,
            arguments = Chat.arguments
        ) {
            val chatViewModel = hiltViewModel<ChatViewModel>()

            it.arguments?.getString(Chat.cidArg)?.let { cid -> chatViewModel.setChannelId(cid) }

            val channelId by chatViewModel.channelId.collectAsStateWithLifecycle()

            ChatScreen(
                channelId,
                onBackPressed = {
                    navController.navigate(Home.route) {
                        popUpTo(Chat.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                    onDestinationChanged(Home)
                },
                onChannelRoomStateChanged = { channelRoomState ->
                    onChannelRoomStateChanged(channelRoomState)
                    chatViewModel.setChannelId(channelRoomState.cid)
                }
            )
        }
        composable(route = More.route) {
            MoreScreen(onUserClicked = { onUserClicked(it) })
        }
    }
}