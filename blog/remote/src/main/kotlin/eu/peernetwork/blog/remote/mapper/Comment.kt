package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.remote.comment.CreateCommentMutation
import eu.peernetwork.blog.remote.comment.GetCommentsQuery

fun GetCommentsQuery.AffectedRow.mapToDomain(): List<Comment> {
    return comments.map {
        Comment(
            id = it.commentid,
            content = it.content,
            author = Author(
                id = it.user.id,
                slug = it.user.slug!!,
                username = it.user.username!!,
                imageUrl = it.user.img!!
            )
        )
    }
}

fun CreateCommentMutation.AffectedRow.mapToDomain(): Comment {
    return Comment(
        id = commentid,
        content = content,
        author = Author(
            id = user.id,
            slug = user.slug!!,
            username = user.username!!,
            imageUrl = user.img!!
        )
    )
}
