package eu.peernetwork.wallet.data.api

import eu.peernetwork.wallet.domain.model.Transfer

interface TransferApi {
    suspend fun get(recipient: String, numberoftokens: Int): Transfer
}