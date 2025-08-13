package eu.peernetwork.blog.ui.event

data class UiModerationEvent(
    val onReport: (String) -> Unit,
    val onSave: (String) -> Unit = {}
)