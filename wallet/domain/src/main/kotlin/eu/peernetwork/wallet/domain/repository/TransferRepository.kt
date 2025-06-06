package eu.peernetwork.wallet.domain.repository

import eu.peernetwork.wallet.domain.model.Transfer
import java.math.BigDecimal

interface TransferRepository {
    suspend fun send(recipient: String, tokens: BigDecimal): Transfer
}