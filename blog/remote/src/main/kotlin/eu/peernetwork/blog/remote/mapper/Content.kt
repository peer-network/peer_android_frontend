package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Media
import eu.peernetwork.blog.domain.model.Status
import eu.peernetwork.blog.remote.advert.ListAdvertisementPostsQuery
import eu.peernetwork.blog.remote.content.CreatePostMutation
import eu.peernetwork.blog.remote.content.GetallpostsQuery
import org.apache.commons.lang3.StringEscapeUtils

fun CreatePostMutation.AffectedRows.mapToDomain(url: String, media: List<Media>): Content {
    return Content(
        id = id,
        title = StringEscapeUtils.unescapeJava(title)
            .replace("\\'", "'")
            .replace("\\\"", "\""),
        description = StringEscapeUtils.unescapeJava(mediadescription)
            .replace("\\'", "'")
            .replace("\\\"", "\""),
        media = media,
        author = Author(
            id = user.id,
            slug = user.slug!!,
            username = user.username!!,
            imageUrl = "$url${user.img}?${System.currentTimeMillis()}",
            following = user.isfollowing!!,
            followed = user.isfollowed!!,
            isAccessible = true,
            status = Status.VISIBLE
        ),
        createdAt = createdat.toString().toTimestamp(),
        type = contenttype.mapToDomain(),
        likes = amountlikes,
        isLiked = isliked,
        isDisliked = isdisliked,
        dislikes = amountdislikes,
        isViewed = isviewed,
        isAccessible = true,
        reported = false,
        status = Status.VISIBLE,
        views = amountviews,
        comment = amountcomments,
        url = this.url
    )
}

fun GetallpostsQuery.AffectedRow.mapToDomain(url: String, media: List<Media>): Content {
    return Content(
        id = id,
        title = StringEscapeUtils.unescapeJava(title)
            .replace("\\'", "'")
            .replace("\\\"", "\""),
        description = StringEscapeUtils.unescapeJava(mediadescription)
            .replace("\\'", "'")
            .replace("\\\"", "\""),
        media = media,
        author = Author(
            id = user.id,
            slug = user.slug!!,
            username = user.username!!,
            imageUrl = "$url${user.img}",
            following = user.isfollowing!!,
            followed = user.isfollowed!!,
            isAccessible = !isHiddenForUsers,
            status = visibilityStatus.mapToDomain()
        ),
        createdAt = createdat.toString().toTimestamp(),
        type = contenttype.mapToDomain(),
        likes = amountlikes,
        isLiked = isliked,
        isDisliked = isdisliked,
        isAccessible = !isHiddenForUsers,
        reported = isreported,
        status = visibilityStatus.mapToDomain(),
        dislikes = amountdislikes,
        isViewed = isviewed,
        views = amountviews,
        comment = amountcomments,
        url = this.url
    )
}

fun ListAdvertisementPostsQuery.Post.mapToDomain(url: String, media: List<Media>): Content {
    return Content(
        id = id,
        title = StringEscapeUtils.unescapeJava(title)
            .replace("\\'", "'").replace("\\\"", "\""),
        description = StringEscapeUtils.unescapeJava(mediadescription)
            .replace("\\'", "'").replace("\\\"", "\""),
        media = media,
        author = Author(
            id = user.id,
            slug = user.slug!!,
            username = user.username!!,
            imageUrl = "$url${user.img}",
            following = user.isfollowing!!,
            followed = user.isfollowed!!,
            isAccessible = !isHiddenForUsers,
            status = visibilityStatus.mapToDomain()
        ),
        createdAt = createdat.toString().toTimestamp(),
        type = contenttype.mapToDomain(),
        likes = amountlikes,
        isLiked = isliked,
        isDisliked = isdisliked,
        dislikes = amountdislikes,
        isViewed = isviewed,
        isAccessible = !isHiddenForUsers,
        reported = isreported,
        status = visibilityStatus.mapToDomain(),
        views = amountviews,
        comment = amountcomments,
        url = this.url
    )
}
