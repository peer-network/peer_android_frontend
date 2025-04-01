package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.remote.content.CreatePostMutation
import eu.peernetwork.blog.remote.content.GetallpostsQuery

fun CreatePostMutation.AffectedRows.mapToDomain(): Content {
    return Content(
        id = id,
        title = title,
        type = contenttype.mapToDomain()
    )
}

fun GetallpostsQuery.AffectedRow.mapToDomain(): Content {
    return Content(
        id = id,
        title = title,
        type = contenttype.mapToDomain()
    )
}
