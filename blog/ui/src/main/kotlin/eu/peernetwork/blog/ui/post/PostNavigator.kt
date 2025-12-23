package eu.peernetwork.blog.ui.post

import androidx.compose.runtime.staticCompositionLocalOf

interface PostNavigator {
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
        val LocalPostNavigator = staticCompositionLocalOf<PostNavigator> {
            error("PostNavigator not provided")
        }
    }
}
