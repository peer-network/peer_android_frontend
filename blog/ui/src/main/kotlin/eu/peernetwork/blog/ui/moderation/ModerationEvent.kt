package eu.peernetwork.blog.ui.moderation

data class ModerationEvent(
    val onReport: (String) -> Unit,
    val onSave: (String) -> Unit = {}
)
