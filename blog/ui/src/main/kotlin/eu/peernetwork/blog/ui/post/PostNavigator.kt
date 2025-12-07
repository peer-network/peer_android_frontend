package eu.peernetwork.blog.ui.post

import androidx.compose.runtime.staticCompositionLocalOf

interface PostNavigator {
    fun navigate(route: Route)

    sealed interface Route {
        data class Profile(val id: String): Route
    }

    companion object {
        val LocalPostNavigator = staticCompositionLocalOf<PostNavigator> {
            error("PostNavigator not provided")
        }
    }
}
