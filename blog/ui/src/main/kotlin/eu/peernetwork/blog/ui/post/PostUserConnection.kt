package eu.peernetwork.blog.ui.post

import eu.peernetwork.core.ui.renderer.Renderer

interface PostUserConnection : Renderer.Stateful<PostUserConnection.Spec> {
    data class Spec(
        val id: String,
        val isFollowing: Boolean,
        val isFollowed: Boolean,
    )
}
