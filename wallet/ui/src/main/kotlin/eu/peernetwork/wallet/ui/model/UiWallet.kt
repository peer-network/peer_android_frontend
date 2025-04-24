package eu.peernetwork.wallet.ui.model

import java.math.BigDecimal

data class UiWallet(val balance: BigDecimal, val rate: Float, val converted: BigDecimal, val currency: String)
