package eu.peernetwork.ads.remote.mapper

import ads.ads.eu.peernetwork.ads.remote.AdvertisementHistoryQuery
import ads.ads.eu.peernetwork.ads.remote.ContentQuery
import eu.peernetwork.ads.domain.model.Content
import eu.peernetwork.ads.remote.model.ContentModel

fun ContentModel.mapToDomain(): Content {
    return Content(
        id = id,
        title = title,
        description = description,
        path = path,
        isAccessible = isAccessible,
        status = status
    )
}

fun AdvertisementHistoryQuery.Advertisement.mapToContent(): ContentModel {
    return ContentModel(
        id = post.id,
        title = post.title,
        description = post.mediadescription,
        path = post.media,
        isAccessible = !post.isHiddenForUsers,
        status = post.visibilityStatus.mapToDomain()
    )
}

fun ContentQuery.AffectedRow.mapToDomain(): Content {
    return Content(
        id = id,
        title = title,
        description = mediadescription,
        path = media,
        isAccessible = !isHiddenForUsers,
        status = visibilityStatus.mapToDomain()
    )
}
