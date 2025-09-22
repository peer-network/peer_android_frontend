package eu.peernetwork.blog.ui.event

interface UiPostListener {
    operator fun invoke(event: Event)

    sealed interface Event {
        data class Mention(val username: String) : Event
        data class Hashtag(val tag: String) : Event
        data class Post(val id: String, val position: Int) : Event
        data class Author(val id: String) : Event
    }
}
