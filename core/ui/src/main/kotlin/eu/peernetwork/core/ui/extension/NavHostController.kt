package eu.peernetwork.core.ui.extension

import androidx.navigation.NavHostController

fun NavHostController.attach(destination: String, popUpTo: Int? = null) {
    navigate(destination) {
        popUpTo(popUpTo ?: graph.id)
        launchSingleTop = true
    }
}

fun NavHostController.attachIfNecessary(destination: String, popUpTo: Int? = null) {
    if (currentDestination?.route != destination) {
        attach(destination, popUpTo)
    }
}

fun NavHostController.navigateIfNecessary(destination: String) {
    if (currentDestination?.route != destination) {
        navigate(destination)
    }
}
