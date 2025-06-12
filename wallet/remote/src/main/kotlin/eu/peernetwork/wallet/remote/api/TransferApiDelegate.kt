package eu.peernetwork.wallet.remote.api

import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.wallet.data.api.TransferApi
import eu.peernetwork.wallet.domain.model.Intent
import eu.peernetwork.wallet.domain.model.Quote
import eu.peernetwork.wallet.domain.model.Receipt
import wallet.wallet.eu.peernetwork.wallet.remote.GetActionPricesQuery
import wallet.wallet.eu.peernetwork.wallet.remote.ResolveTransferMutation
import java.math.BigDecimal
import javax.inject.Inject

class TransferApiDelegate @Inject constructor(
    private val client: RequestClient
) : TransferApi {
    override suspend fun getQuote(intent: Intent): Quote {
        val response = client().query(GetActionPricesQuery()).execute()
        val data = response.getOrThrow().getActionPrices
        response.assertOrThrow(data.status, data.ResponseCode)
        return when(intent) {
            Intent.Like -> Quote(BigDecimal(data.affectedRows.likePrice))
            Intent.DisLike -> Quote(BigDecimal(data.affectedRows.dislikePrice))
            Intent.Comment -> Quote(BigDecimal(data.affectedRows.commentPrice))
            Intent.Post -> Quote(BigDecimal(data.affectedRows.postPrice))
        }
    }

    override suspend fun send(recipient: String, tokens: BigDecimal): Receipt {
        val response = client().mutation(
            ResolveTransferMutation(
                recipient = recipient,
                numberoftokens = tokens.toInt()
            )
        ).execute()
        val data = response.getOrThrow().resolveTransfer
        response.assertOrThrow(data.status, data.ResponseCode)
        return Receipt(
            recipient = recipient,
            price = tokens
        )
    }
}
