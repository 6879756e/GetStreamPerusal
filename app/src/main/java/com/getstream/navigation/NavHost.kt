package com.getstream.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.getstream.features.chat.ChatScreen
import com.getstream.features.home.HomeScreen
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
            ChatScreen(cid)
        }
        composable(route = More.route) {
            Box(modifier = Modifier.fillMaxSize(1f)) {
                Text(text = More.label, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}