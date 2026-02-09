package eu.peernetwork.ads.ui.mapper

import eu.peernetwork.ads.domain.model.Metrics
import eu.peernetwork.ads.ui.model.UiMetrics
import java.util.Locale

fun Metrics.mapToDomain(): UiMetrics {
    return UiMetrics(
        token = token,
        euro = euro,
        likes = likes.formatCount(),
        dislikes = dislikes.formatCount(),
        views = views.formatCount(),
        comments = comments.formatCount(),
        report = report.formatCount()
    )
}

fun Int.formatCount(): String {
    return when {
        this < 1_000 -> toString()
        this < 1_000_000 -> {
            val value = this / 1_000.0
            if (value % 1 == 0.0) {
                "${value.toInt()}k"
            } else {
                String.format(Locale.getDefault(), "%.1fk", value)
            }
        }
        this < 1_000_000_000 -> {
            val value = this / 1_000_000.0
            if (value % 1 == 0.0) {
                "${value.toInt()}M"
            } else {
                String.format(Locale.getDefault(), "%.1fM", value)
            }
        }
        else -> {
            val value = this / 1_000_000_000.0
            if (value % 1 == 0.0) {
                "${value.toInt()}B"
            } else {
                String.format(Locale.getDefault(), "%.1fB", value)
            }
        }
    }
}
