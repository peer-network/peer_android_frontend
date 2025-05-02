package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Sort
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.domain.model.Filter
import type.PostSortType

fun Filter.mapToSortType(): PostSortType? {
    return when (criteria) {
        is Filter.Criteria.Content -> (criteria as Filter.Criteria.Content).sort.mapFromDomain()
        is Filter.Criteria.Reaction -> (criteria as Filter.Criteria.Reaction).engagement.mapFromDomain()
        else -> null
    }
}

fun Sort.mapFromDomain(): PostSortType {
    return when (this) {
        Sort.NEW -> PostSortType.NEWEST
        Sort.TREND -> PostSortType.TRENDING
    }
}

fun Engagement.mapFromDomain(): PostSortType {
    return when (this) {
        Engagement.Content.Like -> PostSortType.LIKES
        Engagement.Content.Dislike -> PostSortType.DISLIKES
        Engagement.Content.View -> PostSortType.VIEWS
        else -> PostSortType.UNKNOWN__
    }
}
