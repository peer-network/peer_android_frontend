package eu.peernetwork.app.mapper

import eu.peernetwork.blog.ui.model.UiFilter
import eu.peernetwork.blog.domain.model.Relation

fun Relation.mapFromDomain(): UiFilter {
    return when(this) {
        Relation.FOLLOWED -> UiFilter.FOLLOWED
        Relation.FOLLOWER -> UiFilter.FOLLOWER
        else -> UiFilter.ALL
    }
}
