package eu.peernetwork.wallet.ui.mapper

import eu.peernetwork.wallet.domain.model.Wallet
import eu.peernetwork.wallet.ui.model.UiWallet

fun Wallet.mapFromDomain(): UiWallet {
    return UiWallet(balance, rate, converted = balance*rate.toBigDecimal(), currency = currency)
}
