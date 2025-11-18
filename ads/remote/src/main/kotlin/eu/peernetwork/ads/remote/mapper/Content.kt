package eu.peernetwork.ads.remote.mapper

import ads.ads.eu.peernetwork.ads.remote.AdvertisementHistoryQuery
import ads.ads.eu.peernetwork.ads.remote.ContentQuery
import eu.peernetwork.ads.domain.model.Content
import eu.peernetwork.ads.remote.model.ContentModel

fun ContentModel.mapToDomain(): Content {
    return Content(
        id = id,
        title = this.title,
        description = this.description,
        image = this.image
    )
}

fun AdvertisementHistoryQuery.Advertisement.mapToContent(): ContentModel {
    return ContentModel(
        id = post.id,
        title = post.title,
        description = post.mediadescription,
        image = post.media
    )
}

fun ContentQuery.AffectedRow.mapToDomain(): Content {
    return Content(
        id = id,
        title = this.title,
        description = this.mediadescription,
        image = this.media
    )
}
