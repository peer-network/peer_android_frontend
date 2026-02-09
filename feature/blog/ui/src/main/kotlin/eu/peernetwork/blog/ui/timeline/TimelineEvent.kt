package eu.peernetwork.blog.ui.timeline

sealed interface TimelineEvent {
    data class Post(val position: Int): TimelineEvent
    data class Boost(
        val id: String,
        val isReported: Boolean,
        val isAccessible: Boolean
    ): TimelineEvent
}
