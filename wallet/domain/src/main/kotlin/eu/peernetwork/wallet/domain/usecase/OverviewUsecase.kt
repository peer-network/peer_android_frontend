package eu.peernetwork.wallet.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.wallet.domain.interactor.WalletInteractor
import eu.peernetwork.wallet.domain.model.Wallet
import javax.inject.Inject

class OverviewUsecase @Inject constructor(
    private val interactor: WalletInteractor
) : SuspendableUseCase<Wallet> {
    override suspend fun invoke(): Wallet {
        return interactor.get()
    }
}
