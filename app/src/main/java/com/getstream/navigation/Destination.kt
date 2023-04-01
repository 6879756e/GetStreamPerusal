package com.getstream.navigation

interface Destination {
    val route: String
}

object Login: Destination {
    override val route = "login"
}

object Home: Destination {
    override val route = "home"
}