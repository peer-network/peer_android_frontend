package eu.peernetwork.app.model

import eu.peernetwork.user.ui.R

enum class UiRelation(val value: Int) {
    ALL(R.string.feed_label),
    FOLLOWED(R.string.follow_label),
    FOLLOWER(R.string.follower_label)
}
