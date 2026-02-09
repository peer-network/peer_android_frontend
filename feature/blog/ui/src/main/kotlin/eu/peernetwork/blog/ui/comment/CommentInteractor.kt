package eu.peernetwork.blog.ui.comment

import androidx.compose.runtime.State
import eu.peernetwork.blog.ui.model.UiComment

interface CommentInteractor {
    fun observe(): State<Map<String, UiComment>>

    fun like(comment: UiComment)

    fun viewLike(id: String)
}
