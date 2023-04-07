package com.getstream.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale

sealed interface Destination {
    val route: String
    val imageVectorOutlined: ImageVector
    val imageVectorFilled: ImageVector
    val label: String

    companion object {
        val allDestinations = listOf(Home, Chat, More)
    }
}

object Home : Destination {
    override val route = "home"
    override val imageVectorOutlined = Icons.Outlined.Home
    override val imageVectorFilled = Icons.Filled.Home
    override val label = route.capitalize(Locale.current)
}

object Chat : Destination {
    override val route = "chat"
    override val imageVectorOutlined = Icons.Rounded.ChatBubbleOutline
    override val imageVectorFilled = Icons.Rounded.ChatBubble
    override val label = route.capitalize(Locale.current)
}

object More : Destination {
    override val route = "more"
    override val imageVectorOutlined = Icons.Outlined.MoreHoriz
    override val imageVectorFilled = Icons.Filled.MoreHoriz
    override val label = route.capitalize(Locale.current)
}