package eu.peernetwork.wallet.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.wallet.domain.model.Intent
import eu.peernetwork.wallet.domain.model.Quote
import eu.peernetwork.wallet.domain.repository.TransactionRepository
import javax.inject.Inject

class QuoteUsecase @Inject constructor(
    private val repository: TransactionRepository
) : ParameterizedSuspendableUseCase<Intent, Quote> {
    override suspend fun invoke(args: Intent): Quote {
        return repository.getQuote(args)
    }
}
