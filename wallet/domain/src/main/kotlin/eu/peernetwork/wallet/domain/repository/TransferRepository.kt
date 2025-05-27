package eu.peernetwork.wallet.domain.repository

import eu.peernetwork.wallet.domain.model.Transfer

interface TransferRepository {
    suspend fun get(recipient: String, numberoftokens: Int): Transfer
}