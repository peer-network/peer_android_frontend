package eu.peernetwork.blog.ui.mapper

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.blog.ui.model.UiVideo

fun Content.mapToVideo(): UiVideo {
    val test = Gson().fromJson<List<UiMedia>>(media, object : TypeToken<List<UiMedia>>() {}.type)
    val videoPath = test.firstOrNull()?.path?.removeSurrounding("\"") ?: ""
    return UiVideo(
        id = id,
        title = title,
        description = description,
        media = "https://media.getpeer.eu$videoPath",
        author = author.mapFromDomain(),
        createdAt = createdAt
    )
}
