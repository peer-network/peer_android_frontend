package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Media
import eu.peernetwork.blog.remote.content.CreatePostMutation
import eu.peernetwork.blog.remote.content.GetallpostsQuery
import org.apache.commons.lang3.StringEscapeUtils

fun CreatePostMutation.AffectedRows.mapToDomain(url: String, media: List<Media>): Content {
    return Content(
        id = id,
        title = StringEscapeUtils.unescapeJava(title).replace("\\'", "'").replace("\\\"", "\""),
        description = StringEscapeUtils.unescapeJava(mediadescription).replace("\\'", "'").replace("\\\"", "\""),
        media = media,
        author = Author(
            id = user.id,
            slug = user.slug!!,
            username = user.username!!,
            imageUrl = "$url${user.img}?${System.currentTimeMillis()}",
            isfollowing = user.isfollowing!!,
            isfollowed = user.isfollowed!!
        ),
        createdAt = createdat.toString().toTimestamp(),
        type = contenttype.mapToDomain(),
        likes = amountlikes,
        isLiked = isliked,
        isDisliked = isdisliked,
        dislikes = amountdislikes,
        comment = amountcomments
    )
}

fun GetallpostsQuery.AffectedRow.mapToDomain(url: String, media: List<Media>): Content {
    return Content(
        id = id,
        title = StringEscapeUtils.unescapeJava(title).replace("\\'", "'").replace("\\\"", "\""),
        description = StringEscapeUtils.unescapeJava(mediadescription).replace("\\'", "'").replace("\\\"", "\""),
        media = media,
        author = Author(
            id = user.id,
            slug = user.slug!!,
            username = user.username!!,
            imageUrl = "$url${user.img}",
            isfollowing = user.isfollowing!!,
            isfollowed = user.isfollowed!!
        ),
        createdAt = createdat.toString().toTimestamp(),
        type = contenttype.mapToDomain(),
        likes = amountlikes,
        isLiked = isliked,
        isDisliked = isdisliked,
        dislikes = amountdislikes,
        comment = amountcomments
    )
}
