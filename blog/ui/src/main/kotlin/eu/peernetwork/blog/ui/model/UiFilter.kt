package eu.peernetwork.blog.ui.model

import eu.peernetwork.blog.ui.R

enum class UiFilter(val value: Int) {
    ALL(R.string.feed_label),
    FOLLOWED(R.string.following_label),
    FOLLOWER(R.string.follower_label),
    MOST_LIKED(R.string.most_liked_label),
    MOST_DISLIKED(R.string.most_disliked_label)
}
