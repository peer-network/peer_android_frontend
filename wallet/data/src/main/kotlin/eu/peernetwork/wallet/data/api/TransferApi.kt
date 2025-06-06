package eu.peernetwork.wallet.data.api

import eu.peernetwork.wallet.domain.model.Transfer
import java.math.BigDecimal

interface TransferApi {
    suspend fun send(recipient: String, tokens: BigDecimal): Transfer
}