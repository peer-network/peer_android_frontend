package eu.peernetwork.blog.ui.article

sealed interface ArticleEvent {
    data class Post(val position: Int): ArticleEvent
    data class Boost(val id: String): ArticleEvent
}
