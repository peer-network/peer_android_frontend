package eu.peernetwork.blog.ui.engagement

import eu.peernetwork.blog.ui.model.UiDraft

sealed class EngagementIntent(val id: String) {
    data class Post(val draft: UiDraft): EngagementIntent(draft.title)
    data class Like(
        val post: String,
        val author: String,
        val message: String,
    ): EngagementIntent(post)
    data class DisLike(val post: String): EngagementIntent(post)
    data class Comment(val post: String): EngagementIntent(post)
}
