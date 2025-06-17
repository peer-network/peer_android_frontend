package eu.peernetwork.wallet.data.repository

import eu.peernetwork.wallet.data.api.TransferApi
import eu.peernetwork.wallet.domain.model.Token
import eu.peernetwork.wallet.domain.model.Quote
import eu.peernetwork.wallet.domain.model.Receipt
import eu.peernetwork.wallet.domain.repository.TransactionRepository
import java.math.BigDecimal
import javax.inject.Inject

class TransactionRepositoryDelegate @Inject constructor(
    private val api: TransferApi
) : TransactionRepository {
    override suspend fun getQuote(token: Token): Quote {
        return api.getQuote(token)
    }

    override suspend fun send(recipient: String, price: BigDecimal): Receipt {
        return api.send(recipient, price)
    }
}
