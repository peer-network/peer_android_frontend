package eu.peernetwork.core.ui.extension

import androidx.navigation.NavBackStackEntry
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

fun NavHostController.navigate(destination: String, backStackEntry: NavBackStackEntry) {
    navigate(destination) {
        popUpTo(backStackEntry.destination.id) {
            inclusive = true
        }
    }
}

fun NavHostController.navigateIfNecessary(destination: String) {
    if (currentDestination?.route != destination) {
        navigate(destination)
    }
}

fun NavHostController.route(destination: String) {
    navigate(destination) {
        launchSingleTop = true
        popUpTo(graph.id)
    }
}
