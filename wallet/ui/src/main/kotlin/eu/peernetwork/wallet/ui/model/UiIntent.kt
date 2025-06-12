package eu.peernetwork.wallet.ui.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface UiIntent {
    data object Post : UiIntent
    data object Like : UiIntent
    data object DisLike : UiIntent
    data object Comment : UiIntent
}
