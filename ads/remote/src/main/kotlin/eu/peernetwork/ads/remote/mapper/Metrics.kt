package eu.peernetwork.ads.remote.mapper

import ads.ads.eu.peernetwork.ads.remote.AdvertisementHistoryQuery
import eu.peernetwork.ads.domain.model.Metrics

fun AdvertisementHistoryQuery.Stats.mapToDomain(): Metrics {
    return Metrics(
        token = tokenSpent.toFloat(),
        euro = euroSpent.toFloat(),
        likes = amountLikes,
        dislikes = amountDislikes,
        views = amountViews,
        comments = amountComments
    )
}
