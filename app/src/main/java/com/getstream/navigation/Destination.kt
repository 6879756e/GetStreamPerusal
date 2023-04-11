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
import androidx.navigation.NavType
import androidx.navigation.navArgument

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
    const val cidArg = "cid"

    override val route = "chat?cid={$cidArg}"
    override val imageVectorOutlined = Icons.Rounded.ChatBubbleOutline
    override val imageVectorFilled = Icons.Rounded.ChatBubble
    override val label = "chat"

    fun channel(cid: String) = "chat?cid=$cid"

    val arguments = listOf(
        navArgument(cidArg) {
            type = NavType.StringType
            nullable = true
        }
    )
}

object More : Destination {
    override val route = "more"
    override val imageVectorOutlined = Icons.Outlined.MoreHoriz
    override val imageVectorFilled = Icons.Filled.MoreHoriz
    override val label = route.capitalize(Locale.current)
}