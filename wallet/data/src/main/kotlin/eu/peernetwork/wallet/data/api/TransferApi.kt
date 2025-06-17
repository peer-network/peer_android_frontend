package eu.peernetwork.wallet.data.api

import eu.peernetwork.wallet.domain.model.Token
import eu.peernetwork.wallet.domain.model.Quote
import eu.peernetwork.wallet.domain.model.Receipt
import java.math.BigDecimal

interface TransferApi {
    suspend fun getQuote(token: Token): Quote

    suspend fun send(recipient: String, tokens: BigDecimal): Receipt
}