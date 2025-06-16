package eu.peernetwork.wallet.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiReward(
    val name: String,
    val used: Int,
    val available: Int
)
