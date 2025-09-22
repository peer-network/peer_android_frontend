package eu.peernetwork.blog.domain.model

sealed interface Engagement {
    sealed interface Content : Engagement {
        data object Like : Content
        data object Dislike : Content
        data object View : Content
        data object Report : Content
        data object Save : Content
        data object LikedComment : Content
    }

    sealed interface Comment : Engagement {
        data object Like : Comment
        data object Report : Comment
    }
}
