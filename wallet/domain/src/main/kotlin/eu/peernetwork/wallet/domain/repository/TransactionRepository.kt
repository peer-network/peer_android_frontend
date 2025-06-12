package eu.peernetwork.wallet.domain.repository

import eu.peernetwork.wallet.domain.model.Intent
import eu.peernetwork.wallet.domain.model.Quote
import eu.peernetwork.wallet.domain.model.Receipt
import java.math.BigDecimal

interface TransactionRepository {
    suspend fun getQuote(intent: Intent): Quote

    suspend fun send(recipient: String, price: BigDecimal): Receipt
}
