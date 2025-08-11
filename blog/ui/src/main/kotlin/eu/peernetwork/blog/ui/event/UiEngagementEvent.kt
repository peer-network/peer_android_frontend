package eu.peernetwork.blog.ui.event

import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.blog.ui.model.UiEngagement

data class UiEngagementEvent(
    val onLoad: (UiContent) -> UiEngagement,
    val onLike: (UiEngagement) -> Unit,
    val onDisLike: (UiEngagement) -> Unit,
    val onComment: (UiContent) -> Unit,
)