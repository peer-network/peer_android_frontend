package eu.peernetwork.blog.ui.engagement

import eu.peernetwork.blog.ui.model.UiDraft

sealed class EngagementType(val id: String) {
    data class Post(val draft: UiDraft): EngagementType(draft.title)
    data class Like(val post: String): EngagementType(post)
    data class DisLike(val post: String): EngagementType(post)
    data class Comment(val post: String): EngagementType(post)
}
