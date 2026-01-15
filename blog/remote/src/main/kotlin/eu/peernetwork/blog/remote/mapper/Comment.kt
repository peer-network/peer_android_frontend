package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.remote.comment.CreateCommentMutation
import eu.peernetwork.blog.remote.comment.GetCommentsQuery
import org.apache.commons.lang3.StringEscapeUtils
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun GetCommentsQuery.AffectedRow.mapToDomain(): List<Comment> {
    return comments.map {
        Comment(
            id = it.commentid,
            content = StringEscapeUtils.unescapeJava(it.content).replace("\\'", "'"),
            author = Author(
                id = it.user.id,
                slug = it.user.slug!!,
                username = it.user.username!!,
                imageUrl = it.user.img!!,
                isfollowing = it.user.isfollowing!!,
                isfollowed = it.user.isfollowed!!
                ),
            createdAt = 0L,
            likes = it.amountlikes,
            isLiked = it.isliked,
            isReported = it.hasActiveReports
        )
    }
}

fun CreateCommentMutation.AffectedRow.mapToDomain(): Comment {
    return Comment(
        id = commentid,
        content = StringEscapeUtils.unescapeJava(content).replace("\\'", "'"),
        author = Author(
            id = user.id,
            slug = user.slug!!,
            username = user.username!!,
            imageUrl = user.img!!,
            isfollowing = user.isfollowing!!,
            isfollowed = user.isfollowed!!
        ),
        createdAt = (createdat.toString()).mapToTimestamp(),
        likes = amountlikes,
        isLiked = isliked
    )
}

fun String.mapToTimestamp(): Long {
    val trimmedDate = substring(0, 23)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.parse(trimmedDate)?.time ?: 0L
}
