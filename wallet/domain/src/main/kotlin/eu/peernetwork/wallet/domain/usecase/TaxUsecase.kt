package eu.peernetwork.wallet.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.wallet.domain.model.Tax
import eu.peernetwork.wallet.domain.repository.TaxRepository
import javax.inject.Inject

class TaxUsecase @Inject constructor(
    private val repository: TaxRepository
) : SuspendableUseCase<Tax> {
    override suspend fun invoke(): Tax {
        return repository.getTax()
    }
}
