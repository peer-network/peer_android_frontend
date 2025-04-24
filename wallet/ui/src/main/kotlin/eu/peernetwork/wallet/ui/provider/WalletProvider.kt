package eu.peernetwork.wallet.ui.provider

import eu.peernetwork.wallet.domain.repository.WalletRepository

interface WalletProvider {
    fun walletRepository(): WalletRepository
}
