package eu.peernetwork.user.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiCoupon(
    val name: String,
    val used: Int,
    val available: Int
)
