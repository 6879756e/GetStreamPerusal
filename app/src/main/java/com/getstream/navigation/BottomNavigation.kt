package com.getstream.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GetStreamPerusalBottomNavigation(
    currentDestination: Destination,
    onItemClicked: (Destination) -> Unit,
) {
    NavigationBar {
        Destination.allDestinations.forEach {
            NavigationBarItem(
                selected = it == currentDestination,
                onClick = { onItemClicked(it) },
                icon = {
                    Icon(
                        imageVector = if (it == currentDestination) it.imageVectorFilled else it.imageVectorOutlined,
                        contentDescription = "Decorative icon for ${it.label}"
                    )
                },
                label = { Text(text = it.label) }
            )
        }
    }
}