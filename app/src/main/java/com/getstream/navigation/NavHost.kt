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

@Composable
fun GetStreamPerusalNavHost(
    navController: NavHostController, modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier,
    ) {
        composable(route = Home.route) {
            Box(modifier = Modifier.fillMaxSize(1f)) {
                Text(text = Home.label, modifier = Modifier.align(Alignment.Center))
            }
        }
        composable(route = Chat.route) {
            Box(modifier = Modifier.fillMaxSize(1f)) {
                Text(text = Chat.label, modifier = Modifier.align(Alignment.Center))
            }
        }
        composable(route = More.route) {
            Box(modifier = Modifier.fillMaxSize(1f)) {
                Text(text = More.label, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}