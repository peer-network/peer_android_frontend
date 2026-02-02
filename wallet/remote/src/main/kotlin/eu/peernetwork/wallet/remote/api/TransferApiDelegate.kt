package eu.peernetwork.wallet.remote.api

import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.wallet.data.api.TransferApi
import eu.peernetwork.wallet.domain.model.Filter
import eu.peernetwork.wallet.domain.model.Token
import eu.peernetwork.wallet.domain.model.Quote
import eu.peernetwork.wallet.domain.model.Receipt
import eu.peernetwork.wallet.domain.model.Sort
import eu.peernetwork.wallet.domain.model.Transaction
import eu.peernetwork.wallet.remote.mapper.mapFromDomain
import eu.peernetwork.wallet.remote.mapper.mapToDomain
import eu.peernetwork.wallet.remote.mapper.mapToType
import wallet.wallet.eu.peernetwork.wallet.remote.GetActionPricesQuery
import wallet.wallet.eu.peernetwork.wallet.remote.ResolveTransferMutation
import wallet.wallet.eu.peernetwork.wallet.remote.TransactionHistoryQuery
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Named

class TransferApiDelegate @Inject constructor(
    private val client: RequestClient,
    @Named("mediaUrl") private val url: String,
) : TransferApi {
    override suspend fun getAll(filter: Filter, sort: Sort, page: Pageable): Page<Transaction> {
        val query = TransactionHistoryQuery(
            type = Optional.presentIfNotNull(filter.mapToType()),
            limit = Optional.present(page.limit),
            offset = Optional.present(page.offset),
            sort = Optional.present(sort.mapFromDomain())
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().transactionHistory
        response.assertOrThrow(data.meta?.status, data.meta?.ResponseCode)
        val transactions = data.affectedRows?.map { transaction ->
            transaction.mapToDomain().let {
                it.copy(sender = it.sender.copy(imageUrl = "$url${it.sender.imageUrl}"))
                    .copy(recipient = it.recipient.copy(imageUrl = "$url${it.recipient.imageUrl}"))
            }
        }
        return Page(
            count = transactions?.size ?: 0,
            offset = page.offset,
            items = transactions ?: emptyList()
        )
    }

    override suspend fun getQuote(token: Token): Quote {
        val response = client().query(GetActionPricesQuery()).execute()
        val data = response.getOrThrow().getActionPrices
        response.assertOrThrow(data.status, data.ResponseCode)
        return when(token) {
            Token.Like -> Quote(BigDecimal(data.affectedRows.likePrice))
            Token.DisLike -> Quote(BigDecimal(data.affectedRows.dislikePrice))
            Token.Comment -> Quote(BigDecimal(data.affectedRows.commentPrice))
            Token.Post -> Quote(BigDecimal(data.affectedRows.postPrice))
        }
    }

    override suspend fun send(recipient: String, tokens: BigDecimal, message: String?): Receipt {
        val response = client().mutation(
            ResolveTransferMutation(
                recipient = recipient,
                message = Optional.presentIfNotNull(message),
                numberoftokens = tokens.toInt()
            )
        ).execute()
        val data = response.getOrThrow().resolveTransferV2
        response.assertOrThrow(data.status, data.ResponseCode)
        return Receipt(
            recipient = recipient,
            price = tokens
        )
    }
}
