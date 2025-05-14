package eu.peernetwork.core.ui.extension

import androidx.navigation.NavHostController

fun NavHostController.attach(destination: String, popUpTo: Int? = null) {
    navigate(destination) {
        launchSingleTop = true
        restoreState = true
        popUpTo(popUpTo ?: graph.id) {
            saveState = true
        }
    }
}

fun NavHostController.attachIfNecessary(destination: String, popUpTo: Int? = null): Boolean {
    if (currentDestination?.route != destination) {
        attach(destination, popUpTo)
        return true
    }
    return false
}

fun NavHostController.navigateIfNecessary(destination: String) {
    if (currentDestination?.route != destination) {
        navigate(destination)
    }
}
