package eu.peernetwork.wallet.data.interactor

import eu.peernetwork.wallet.domain.interactor.WalletInteractor
import eu.peernetwork.wallet.domain.model.Transfer
import eu.peernetwork.wallet.domain.model.Wallet
import eu.peernetwork.wallet.domain.repository.TransferRepository
import eu.peernetwork.wallet.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import java.math.BigDecimal
import javax.inject.Inject

class WalletInteractorDelegate @Inject constructor(
    private val walletRepository: WalletRepository,
    private val transferRepository: TransferRepository,
) : WalletInteractor {
    private val mutableWallet = MutableSharedFlow<Wallet>(replay = 1)

    override suspend fun get(): Wallet {
        val wallet = walletRepository.get()
        mutableWallet.tryEmit(wallet)
        return wallet
    }

    override fun observe(): Flow<Wallet> = mutableWallet

    override suspend fun send(recipient: String, token: BigDecimal): Transfer {
        val transfer = transferRepository.send(recipient, token)
        if (mutableWallet.replayCache.isNotEmpty()) {
            mutableWallet.firstOrNull()?.let {
                mutableWallet.tryEmit(it.copy(balance = it.balance - token))
            }
        } else {
            get()
        }
        return transfer
    }
}
