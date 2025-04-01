package eu.peernetwork.blog.remote.mock

import eu.peernetwork.blog.remote.comment.CreateCommentMutation
import eu.peernetwork.blog.remote.comment.GetCommentsQuery
import eu.peernetwork.core.remote.model.Status

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
                    createdat = "<test-createdat>",
                    amountlikes = 42,
                    isliked = true,
                    user = CreateCommentMutation.User(
                        id = "<test-userid>",
                        username = "<test-username>",
                        img = "<test-avatar>",
                        slug = 0,
                        isfollowed = false,
                        isfollowing = false
                    )
                )
            )
        )
    }

    fun comments(): GetCommentsQuery.Getallposts {
        return GetCommentsQuery.Getallposts(
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
                            createdat = "<test-createdat>",
                            amountlikes = 42,
                            isliked = true,
                            user = GetCommentsQuery.User(
                                id = "<test-userid>",
                                username = "<test-username>",
                                img = "<test-avatar>",
                                slug = 0,
                                isfollowed = false,
                                isfollowing = false
                            )
                        )
                    )
                )
            )
        )
    }
}
