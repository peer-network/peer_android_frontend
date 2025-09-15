package eu.peernetwork.app.mapper

import eu.peernetwork.blog.ui.model.UiFilter
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Sort

fun UiFilter.mapToCriteria(
    tag: String? = null,
    title: String? = null,
): Criteria {
    return when(this) {
        UiFilter.MOST_LIKED -> Criteria.Content(
            sort = Sort.MOST_LIKED,
            tag = tag,
            title = title
        )
        UiFilter.MOST_VIEWED -> Criteria.Content(
            sort = Sort.MOST_VIEWED,
            tag = tag,
            title = title
        )
        UiFilter.MOST_DISLIKED -> Criteria.Content(
            sort = Sort.MOST_DISLIKED,
            tag = tag,
            title = title
        )
        else -> Criteria.None
    }
}
