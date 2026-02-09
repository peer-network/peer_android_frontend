package eu.peernetwork.wallet.ui.provider

import eu.peernetwork.wallet.domain.repository.RewardRepository
import eu.peernetwork.wallet.domain.repository.TaxRepository
import eu.peernetwork.wallet.domain.repository.TransactionRepository
import eu.peernetwork.wallet.domain.repository.WalletRepository

interface RepositoryProvider {
    fun taxRepository(): TaxRepository

    fun walletRepository(): WalletRepository

    fun transferRepository(): TransactionRepository

    fun rewardRepository(): RewardRepository
}
