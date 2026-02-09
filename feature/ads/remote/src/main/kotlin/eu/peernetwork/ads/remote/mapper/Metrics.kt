package eu.peernetwork.ads.remote.mapper

import ads.ads.eu.peernetwork.ads.remote.AdvertisementHistoryQuery
import eu.peernetwork.ads.domain.model.Metrics

fun AdvertisementHistoryQuery.Stats.mapToDomain(): Metrics {
    return Metrics(
        token = tokenSpent.toFloat(),
        euro = gemsEarned.toFloat(),
        likes = amountLikes,
        dislikes = amountDislikes,
        views = amountViews,
        comments = amountComments,
        report = amountReports
    )
}

fun AdvertisementHistoryQuery.Advertisement.mapToMetrics(): Metrics {
    return Metrics(
        token = totalTokenCost.toFloat(),
        euro = gemsEarned.toFloat(),
        likes = amountLikes,
        dislikes = amountDislikes,
        views = amountViews,
        comments = amountComments,
        report = amountReports
    )
}
