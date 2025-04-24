package eu.peernetwork.wallet.data.api

import eu.peernetwork.wallet.domain.model.Wallet

interface WalletApi {
    suspend fun get(): Wallet
}
