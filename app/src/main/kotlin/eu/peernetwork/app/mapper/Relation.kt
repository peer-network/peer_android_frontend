package eu.peernetwork.app.mapper

import eu.peernetwork.app.model.UiRelation
import eu.peernetwork.blog.domain.model.Relation

fun Relation.mapFromDomain(): UiRelation {
    return when(this) {
        Relation.FOLLOWED -> UiRelation.FOLLOWED
        Relation.FOLLOWER -> UiRelation.FOLLOWER
        else -> UiRelation.ALL
    }
}
