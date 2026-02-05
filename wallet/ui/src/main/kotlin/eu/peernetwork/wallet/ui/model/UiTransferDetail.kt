package eu.peernetwork.wallet.ui.model

import java.math.BigDecimal

data class UiTransferDetail(
    val amount: BigDecimal,
    val message: String,
    val recipient: UiRecipient
)
