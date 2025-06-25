package eu.peernetwork.blog.ui.moderation

data class Moderations(
    val onReport: (String) -> Unit,
    val onSave: (String) -> Unit = {}
)
