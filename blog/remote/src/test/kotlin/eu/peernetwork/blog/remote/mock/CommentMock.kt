package eu.peernetwork.blog.remote.mock

import eu.peernetwork.blog.remote.comment.CreateCommentMutation
import eu.peernetwork.blog.remote.comment.GetCommentsQuery
import eu.peernetwork.core.remote.model.Status
import type.ContentVisibilityStatus

object CommentMock {
    fun comment(): CreateCommentMutation.CreateComment {
        return CreateCommentMutation.CreateComment(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            counter = 1,
            affectedRows = listOf(
                CreateCommentMutation.AffectedRow(
                    commentid = "<test-commentid>",
                    postid = "<test-postid>",
                    parentid = "<test-parentid>",
                    content = "<test-content>",
                    createdat = "2025-03-24 00:05:09.334000",
                    amountlikes = 42,
                    isliked = true,
                    isHiddenForUsers = true,
                    visibilityStatus = ContentVisibilityStatus.HIDDEN,
                    user = CreateCommentMutation.User(
                        id = "<test-userid>",
                        username = "<test-username>",
                        img = "<test-avatar>",
                        slug = 0,
                        isfollowed = false,
                        isfollowing = false,
                        isHiddenForUsers = true,
                        visibilityStatus = ContentVisibilityStatus.HIDDEN
                    )
                )
            )
        )
    }

    fun comments(): GetCommentsQuery.ListPosts {
        return GetCommentsQuery.ListPosts(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            counter = 1,
            affectedRows = listOf(
                GetCommentsQuery.AffectedRow(
                    amountcomments = 50,
                    comments = listOf(
                        GetCommentsQuery.Comment(
                            commentid = "<test-commentid>",
                            postid = "<test-postid>",
                            parentid = "<test-parentid>",
                            content = "<test-content>",
                            createdat = "2025-03-24 00:05:09.334000",
                            amountlikes = 42,
                            isliked = true,
                            hasActiveReports = false,
                            isHiddenForUsers = true,
                            visibilityStatus = ContentVisibilityStatus.HIDDEN,
                            user = GetCommentsQuery.User(
                                id = "<test-userid>",
                                username = "<test-username>",
                                img = "<test-avatar>",
                                slug = 0,
                                isfollowed = false,
                                isfollowing = false,
                                isHiddenForUsers = true,
                                visibilityStatus = ContentVisibilityStatus.HIDDEN
                            )
                        )
                    )
                )
            )
        )
    }
}
