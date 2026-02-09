package eu.peernetwork.user.ui.user

import androidx.compose.runtime.staticCompositionLocalOf

interface UserNavigator {
    fun navigate(route: Route)

    sealed interface Route {
        data class Search(
            val type: String,
            val query: String
        ): Route
        data class Link(val url: String): Route
        data class Profile(val id: String): Route
    }

    companion object {
        val LocalUserNavigator = staticCompositionLocalOf<UserNavigator> {
            error("${UserNavigator::class.java.name} not provided")
        }
    }
}
