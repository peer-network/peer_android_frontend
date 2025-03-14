package eu.peernetwork.core.ui.extension

import androidx.navigation.NavHostController

fun NavHostController.attach(destination: String) {
    navigate(destination) {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}
