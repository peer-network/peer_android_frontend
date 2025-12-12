package eu.peernetwork.wallet.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.wallet.domain.model.Token
import eu.peernetwork.wallet.domain.model.Quote
import eu.peernetwork.wallet.domain.repository.TransactionRepository
import javax.inject.Inject

class QuoteUsecase @Inject constructor(
    private val repository: TransactionRepository
) : ParameterizedSuspendableUseCase<Token, Quote> {
    override suspend fun invoke(param: Token): Quote {
        return repository.getQuote(param)
    }
}
