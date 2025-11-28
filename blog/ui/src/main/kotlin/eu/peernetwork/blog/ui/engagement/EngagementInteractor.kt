package eu.peernetwork.blog.ui.engagement

import eu.peernetwork.blog.ui.model.UiReaction
import eu.peernetwork.blog.ui.model.v2.UiEngagement
import eu.peernetwork.blog.ui.model.v2.UiPostDetail

interface EngagementInteractor {
    fun observe(): androidx.compose.runtime.State<Map<String, UiReaction>>

    operator fun invoke(state: State)

    sealed interface State {
        data class Like(
            val id: String,
            val author: String,
            val message: String,
        ) : State
        data class Dislike(val id: String) : State
        data class Comment(val model: UiPostDetail) : State
        data class View(val engagement: UiEngagement) : State
    }
}
