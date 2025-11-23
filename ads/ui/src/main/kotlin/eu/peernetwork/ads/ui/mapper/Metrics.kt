package eu.peernetwork.ads.ui.mapper

import eu.peernetwork.ads.domain.model.Metrics
import eu.peernetwork.ads.ui.model.UiMetrics

fun Metrics.mapToDomain(): UiMetrics {
    return UiMetrics(
        token = token,
        euro = euro,
        likes = likes,
        dislikes = dislikes,
        views = views,
        comments = comments,
        report = report
    )
}
