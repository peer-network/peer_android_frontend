package eu.peernetwork.wallet.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.wallet.domain.model.Wallet
import eu.peernetwork.wallet.domain.repository.WalletRepository
import javax.inject.Inject

class OverviewUsecase @Inject constructor(
    private val repository: WalletRepository
) : SuspendableUseCase<Wallet> {
    override suspend fun invoke(): Wallet {
        return repository.get()
    }
}
