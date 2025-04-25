package eu.peernetwork.wallet.data.repository

import eu.peernetwork.wallet.data.api.WalletApi
import eu.peernetwork.wallet.domain.model.Wallet
import eu.peernetwork.wallet.domain.repository.WalletRepository
import javax.inject.Inject

class WalletRepositoryDelegate @Inject constructor(
    private val api: WalletApi
) : WalletRepository {
    override suspend fun get(): Wallet {
        return api.get()
    }
}
