package eu.peernetwork.blog.ui.engagement

import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.blog.ui.model.UiDraft

sealed class EngagementEvent(val id: String) {
    data class Post(val draft: UiDraft): EngagementEvent(draft.title)
    data class Like(val content: UiContent): EngagementEvent(content.id)
    data class DisLike(val post: String): EngagementEvent(post)
    data class Comment(val post: String): EngagementEvent(post)
}
