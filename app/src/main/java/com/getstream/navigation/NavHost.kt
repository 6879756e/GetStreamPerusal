package com.getstream.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.getstream.features.chat.ChatScreen
import com.getstream.features.home.HomeScreen
import com.getstream.features.more.MoreScreen
import io.getstream.chat.android.client.models.User

@Composable
fun GetStreamPerusalNavHost(
    navController: NavHostController,
    modifier: Modifier,
    onDestinationChanged: (Destination) -> Unit,
    onUserClicked: (User) -> Unit,
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
            val cid = it.arguments?.getString(Chat.cidArg)
            ChatScreen(cid) {
                navController.navigate(Home.route)
                onDestinationChanged(Home)
            }
        }
        composable(route = More.route) {
            MoreScreen(onUserClicked = { onUserClicked(it) })
        }
    }
}