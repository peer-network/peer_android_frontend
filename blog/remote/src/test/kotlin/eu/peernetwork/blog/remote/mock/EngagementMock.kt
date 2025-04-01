package eu.peernetwork.blog.remote.mock

import eu.peernetwork.blog.remote.engagement.ResolveActionPostMutation.ResolveActionPost
import eu.peernetwork.core.remote.model.Status

object Engagement {
    fun response(): ResolveActionPost {
        return ResolveActionPost(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }
}