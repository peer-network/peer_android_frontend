package eu.peernetwork.wallet.ui.mapper

import eu.peernetwork.wallet.domain.model.Intent
import eu.peernetwork.wallet.ui.model.UiIntent

fun UiIntent.mapToDomain(): Intent {
    return when(this) {
        UiIntent.Like -> Intent.Like
        UiIntent.DisLike -> Intent.DisLike
        UiIntent.Comment -> Intent.Comment
        UiIntent.Post -> Intent.Post
    }
}
