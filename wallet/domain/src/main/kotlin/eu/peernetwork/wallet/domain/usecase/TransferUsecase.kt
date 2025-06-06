package eu.peernetwork.wallet.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.wallet.domain.interactor.WalletInteractor
import eu.peernetwork.wallet.domain.model.Transfer
import java.math.BigDecimal
import javax.inject.Inject

class TransferUsecase @Inject constructor(
    private val interactor: WalletInteractor
) : ParameterizedSuspendableUseCase<TransferUsecase.Parameter, Transfer> {
    override suspend fun invoke(param: Parameter): Transfer {
        return interactor.send(param.recipient, param.tokens)
    }

    data class Parameter(
        val recipient: String,
        val tokens: BigDecimal
    )
}