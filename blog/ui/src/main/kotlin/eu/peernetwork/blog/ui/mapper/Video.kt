package eu.peernetwork.blog.ui.mapper

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.blog.ui.model.UiVideo

fun Content.mapToVideo(): UiVideo {
    return UiVideo(
        id = id,
        title = title,
        description = "",
        media = media.first().path,
        author = author.mapFromDomain(),
        createdAt = createdAt
    )
}
