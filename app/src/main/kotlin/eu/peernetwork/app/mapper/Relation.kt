package eu.peernetwork.app.mapper

import eu.peernetwork.blog.ui.model.UiFilter
import eu.peernetwork.blog.domain.model.Category

fun Category.mapFromDomain(): UiFilter {
    return when(this) {
        Category.FOLLOWED -> UiFilter.FOLLOWED
        Category.FOLLOWER -> UiFilter.FOLLOWER
        Category.MOST_LIKED -> UiFilter.MOST_LIKED
        Category.MOST_DISLIKED -> UiFilter.MOST_DISLIKED
        else -> UiFilter.ALL
    }
}
