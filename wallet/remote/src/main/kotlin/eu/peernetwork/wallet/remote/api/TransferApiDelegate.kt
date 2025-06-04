package eu.peernetwork.wallet.remote.api

import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.wallet.data.api.TransferApi
import eu.peernetwork.wallet.domain.model.Transfer
import wallet.wallet.eu.peernetwork.wallet.remote.ResolveTransferMutation
import java.math.BigDecimal
import javax.inject.Inject

class TransferApiDelegate @Inject constructor(
    private val client: RequestClient
) : TransferApi {
    override suspend fun get(recipient: String, tokens: BigDecimal): Transfer {
        val response = client().mutation(
            ResolveTransferMutation(
                recipient = recipient,
                numberoftokens = tokens.toInt()
            )
        ).execute()
        val data = response.getOrThrow().resolveTransfer
        response.assertOrThrow(data.status, data.ResponseCode)
        return Transfer(
            recipient = recipient,
            token = tokens
        )
    }
}
