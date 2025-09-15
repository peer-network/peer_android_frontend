package eu.peernetwork.blog.ui.model

import eu.peernetwork.blog.ui.R

enum class UiFilter(val value: Int) {
    NONE(R.string.feed_label),
    TRENDS(R.string.trend_label),
    MOST_LIKED(R.string.most_liked_label),
    MOST_VIEWED(R.string.most_viewed_label),
    MOST_DISLIKED(R.string.most_disliked_label)
}
