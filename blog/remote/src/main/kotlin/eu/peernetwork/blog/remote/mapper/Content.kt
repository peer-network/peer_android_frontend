package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Media
import eu.peernetwork.blog.remote.content.CreatePostMutation
import eu.peernetwork.blog.remote.content.GetallpostsQuery

fun CreatePostMutation.AffectedRows.mapToDomain(media: List<Media>): Content {
    return Content(
        id = id,
        title = title,
        description = mediadescription,
        media = media,
        author = Author(
            id = user.id,
            slug = user.slug!!,
            username = user.username!!,
            imageUrl = user.img!!
        ),
        createdAt = createdat.toString().toTimestamp(),
        type = contenttype.mapToDomain()
    )
}

fun GetallpostsQuery.AffectedRow.mapToDomain(media: List<Media>): Content {
    return Content(
        id = id,
        title = title,
        description = mediadescription,
        media = media,
        author = Author(
            id = user.id,
            slug = user.slug!!,
            username = user.username!!,
            imageUrl = user.img!!
        ),
        createdAt = createdat.toString().toTimestamp(),
        type = contenttype.mapToDomain()
    )
}
