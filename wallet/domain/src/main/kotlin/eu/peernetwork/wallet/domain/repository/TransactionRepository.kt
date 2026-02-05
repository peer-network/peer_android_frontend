package eu.peernetwork.wallet.domain.repository

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.wallet.domain.model.Filter
import eu.peernetwork.wallet.domain.model.Token
import eu.peernetwork.wallet.domain.model.Quote
import eu.peernetwork.wallet.domain.model.Receipt
import eu.peernetwork.wallet.domain.model.Sort
import eu.peernetwork.wallet.domain.model.Transaction
import java.math.BigDecimal

interface TransactionRepository {
    suspend fun getAll(
        filter: Filter = Filter.None,
        sort: Sort = Sort.NEWEST,
        page: Pageable
    ): Page<Transaction>

    suspend fun getQuote(token: Token): Quote

    suspend fun send(recipient: String, price: BigDecimal, message: String? = null): Receipt
}
