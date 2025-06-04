package eu.peernetwork.wallet.ui.model

import androidx.compose.runtime.Immutable
import java.math.BigDecimal

@Immutable
data class UiTransfer(
    val recipient: String,
    val token: BigDecimal
)
