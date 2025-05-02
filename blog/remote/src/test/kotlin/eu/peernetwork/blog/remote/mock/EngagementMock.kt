package eu.peernetwork.blog.remote.mock

import eu.peernetwork.blog.remote.engagement.LikeCommentMutation.LikeComment
import eu.peernetwork.blog.remote.engagement.ReportCommentMutation.ReportComment
import eu.peernetwork.blog.remote.engagement.ResolveActionPostMutation.ResolvePostAction
import eu.peernetwork.core.remote.model.Status

object EngagementMock {
    fun postResponse(): ResolvePostAction {
        return ResolvePostAction(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }

    fun commentResponse(): LikeComment {
        return LikeComment(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }

    fun reportResponse(): ReportComment {
        return ReportComment(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }
}
