package eu.peernetwork.blog.ui.timeline

sealed interface TimelineEvent {
    data class Post(val position: Int): TimelineEvent
}
