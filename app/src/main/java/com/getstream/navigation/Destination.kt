package com.getstream.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale

sealed interface Destination {
    val route: String
    val imageVector: ImageVector
    val label: String

    companion object {
        val allDestinations = listOf(Home, Chat, More)
    }
}

object Home : Destination {
    override val route = "home"
    override val imageVector = Icons.Outlined.Home
    override val label = route.capitalize(Locale.current)
}

object Chat : Destination {
    override val route = "chat"
    override val imageVector = Icons.Outlined.Chat
    override val label = route.capitalize(Locale.current)
}

object More : Destination {
    override val route = "more"
    override val imageVector = Icons.Outlined.MoreHoriz
    override val label = route.capitalize(Locale.current)
}