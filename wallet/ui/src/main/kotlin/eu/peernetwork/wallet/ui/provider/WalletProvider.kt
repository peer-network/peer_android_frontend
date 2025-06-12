package eu.peernetwork.wallet.ui.provider

import eu.peernetwork.wallet.domain.interactor.WalletInteractor
import eu.peernetwork.wallet.domain.repository.TransactionRepository
import eu.peernetwork.wallet.domain.repository.WalletRepository

interface WalletProvider {
    fun walletRepository(): WalletRepository

    fun transferRepository(): TransactionRepository

    fun walletInteractor(): WalletInteractor
}
