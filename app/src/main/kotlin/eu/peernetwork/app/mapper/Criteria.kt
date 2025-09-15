package eu.peernetwork.app.mapper

import eu.peernetwork.blog.ui.model.UiFilter
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Sort

fun UiFilter.mapToCriteria(
    sort: Sort? = null,
    tag: String? = null,
    title: String? = null,
): Criteria {
    val sortBy = sort ?: Sort.NEW
    return when(this) {
        UiFilter.MOST_LIKED -> Criteria.Reaction(
            engagement = Engagement.Content.Like,
            sortBy = sortBy,
            tagFilter = tag,
            titleFilter = title
        )
        UiFilter.MOST_VIEWED -> Criteria.Reaction(
            engagement = Engagement.Content.View,
            sortBy = sortBy,
            tagFilter = tag,
            titleFilter = title
        )
        UiFilter.MOST_DISLIKED -> Criteria.Reaction(
            engagement = Engagement.Content.Dislike,
            sortBy = sortBy,
            tagFilter = tag,
            titleFilter = title
        )
        else -> Criteria.Content(
            sortBy = sortBy,
            tagFilter = tag,
            titleFilter = title
        )
    }
}
