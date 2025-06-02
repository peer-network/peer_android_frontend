package eu.peernetwork.wallet.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.wallet.domain.model.Transfer
import eu.peernetwork.wallet.domain.repository.TransferRepository
import javax.inject.Inject

class TransferUsecase @Inject constructor(
    private val repository: TransferRepository
) : ParameterizedSuspendableUseCase<TransferUsecase.Parameter, Transfer> {
    override suspend fun invoke(param: Parameter): Transfer {
        return repository.get(param.recipient, param.numberOfTokens)
    }

    data class Parameter(
        val recipient: String,
        val numberOfTokens: Int
    )
}