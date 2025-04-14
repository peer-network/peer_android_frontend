package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Sort
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.domain.model.Filter
import type.SortType

fun Filter.mapToSortType(): SortType? {
    return when (criteria) {
        is Filter.Criteria.Content -> (criteria as Filter.Criteria.Content).sort.mapFromDomain()
        is Filter.Criteria.Reaction -> (criteria as Filter.Criteria.Reaction).engagement.mapFromDomain()
        else -> null
    }
}

fun Sort.mapFromDomain(): SortType {
    return when (this) {
        Sort.NEW -> SortType.NEWEST
        Sort.TREND -> SortType.TRENDING
    }
}

fun Engagement.mapFromDomain(): SortType {
    return when (this) {
        Engagement.Content.Like -> SortType.LIKES
        Engagement.Content.Dislike -> SortType.DISLIKES
        Engagement.Content.View -> SortType.VIEWS
        else -> SortType.UNKNOWN__
    }
}
