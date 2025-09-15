package eu.peernetwork.blog.domain.model

import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Engagement

/**
 * Map Category to an optional reaction-based sorting Criteria.
 * Followers/Following/All do not apply reaction sorting.
 */
fun Category.toCriteriaOrNull(): Criteria? = when (this) {
    Category.MOST_LIKED -> Criteria.Reaction(Engagement.Content.Like)
    Category.MOST_DISLIKED -> Criteria.Reaction(Engagement.Content.Dislike)
    else -> null
}