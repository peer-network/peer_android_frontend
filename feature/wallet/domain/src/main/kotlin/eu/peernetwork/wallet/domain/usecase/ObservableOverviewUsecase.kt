package eu.peernetwork.wallet.domain.usecase

import eu.peernetwork.core.common.usecase.ObservableUseCase
import eu.peernetwork.wallet.domain.interactor.WalletInteractor
import eu.peernetwork.wallet.domain.model.Wallet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservableOverviewUsecase @Inject constructor(
    private val interactor: WalletInteractor
) : ObservableUseCase<Wallet> {
    override fun invoke(): Flow<Wallet> {
        return interactor.observe()
    }
}
