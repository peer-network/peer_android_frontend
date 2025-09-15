package eu.peernetwork.blog.remote.mapper

import eu.peernetwork.blog.domain.model.Sort
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.domain.model.Filter
import type.PostSortType

fun Filter.engagementFilter(): PostSortType? {
    return (criteria as? Filter.Criteria.Content?)?.sort?.mapFromDomain()
}

fun Sort.mapFromDomain(): PostSortType? {
    return when (this) {
        Sort.NEW -> PostSortType.NEWEST
        Sort.TREND -> PostSortType.TRENDING
        Sort.MOST_LIKED -> PostSortType.LIKES
        Sort.MOST_VIEWED -> PostSortType.VIEWS
        Sort.MOST_DISLIKED -> PostSortType.DISLIKES
    }
}

fun Engagement.mapFromDomain(): PostSortType? {
    return when (this) {
        Engagement.Content.Like -> PostSortType.LIKES
        Engagement.Content.Dislike -> PostSortType.DISLIKES
        Engagement.Content.View -> PostSortType.VIEWS
        else -> null
    }
}
