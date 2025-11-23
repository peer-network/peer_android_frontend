package eu.peernetwork.ads.ui.mapper

import eu.peernetwork.ads.domain.model.Content
import eu.peernetwork.ads.ui.model.UiContent
import eu.peernetwork.core.ui.extension.annotate

fun Content.mapToDomain(): UiContent {
    return UiContent(
        id = id,
        title = title.annotate(),
        description = description.annotate(),
        path = path
    )
}
