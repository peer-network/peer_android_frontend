package eu.peernetwork.wallet.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiTransfer(
    val recipient: String,
    val numberOfToken: Int
)
