package eu.peernetwork.blog.ui.moderation

data class ModerationAction(
    val onReport: (String) -> Unit,
    val onSave: (String) -> Unit = {}
)
