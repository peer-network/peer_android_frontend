package eu.peernetwork.wallet.ui.provider

import eu.peernetwork.wallet.domain.repository.TransferRepository
import eu.peernetwork.wallet.domain.repository.WalletRepository

interface WalletProvider {
    fun walletRepository(): WalletRepository
    fun transferRepository(): TransferRepository
}
