package eu.peernetwork.blog.ui.post

import eu.peernetwork.blog.ui.engagement.EngagementInteractor
import eu.peernetwork.blog.ui.engagement.EngagementReaction
import eu.peernetwork.blog.ui.moderation.ModerationInteractor
import eu.peernetwork.blog.ui.post.Post.Component

interface PostInteractor {
    fun component(): Component

    fun reaction(): EngagementReaction

    fun engagement(): EngagementInteractor

    fun moderation(): ModerationInteractor
}
