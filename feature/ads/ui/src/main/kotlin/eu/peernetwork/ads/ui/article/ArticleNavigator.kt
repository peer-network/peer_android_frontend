package eu.peernetwork.ads.ui.article

import androidx.compose.runtime.staticCompositionLocalOf

interface ArticleNavigator {
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
        val LocalArticleNavigator = staticCompositionLocalOf<ArticleNavigator> {
            error("ArticleNavigator not provided")
        }
    }
}
