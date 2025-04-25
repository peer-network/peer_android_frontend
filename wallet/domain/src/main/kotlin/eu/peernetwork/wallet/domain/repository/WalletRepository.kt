package eu.peernetwork.wallet.domain.repository

import eu.peernetwork.wallet.domain.model.Wallet

interface WalletRepository {
    suspend fun get(): Wallet
}
