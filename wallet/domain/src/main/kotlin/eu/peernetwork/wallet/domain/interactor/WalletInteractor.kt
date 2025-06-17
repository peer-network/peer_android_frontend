package eu.peernetwork.wallet.domain.interactor

import eu.peernetwork.wallet.domain.model.Receipt
import eu.peernetwork.wallet.domain.model.Wallet
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface WalletInteractor {
    suspend fun get(): Wallet

    fun observe(): Flow<Wallet>

    suspend fun send(recipient: String, token: BigDecimal): Receipt
}
