package eu.peernetwork.blog.ui.engagement

import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.blog.ui.model.UiEngagement

data class Engagements(
    val onLoad: (UiContent) -> UiEngagement,
    val onLike: (UiEngagement) -> Unit,
    val onDisLike: (UiEngagement) -> Unit,
    val onComment: (UiContent) -> Unit,
)
