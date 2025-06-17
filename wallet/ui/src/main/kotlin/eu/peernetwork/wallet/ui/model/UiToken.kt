package eu.peernetwork.wallet.ui.model

import androidx.compose.runtime.Immutable

@Immutable
sealed class UiToken(val name: String) {
    data object Post : UiToken("Posts")
    data object Like : UiToken("Likes")
    data object DisLike : UiToken("Dislikes")
    data object Comment : UiToken("Comments")
}
