package eu.peernetwork.wallet.data.repository

import eu.peernetwork.wallet.data.api.TransferApi
import eu.peernetwork.wallet.domain.model.Transfer
import eu.peernetwork.wallet.domain.repository.TransferRepository
import javax.inject.Inject

class TransferRepositoryDelegate @Inject constructor(
    private val api: TransferApi
) : TransferRepository {
    override suspend fun get(recipient: String, numberoftokens: Int): Transfer {
        return api.get(recipient, numberoftokens)
    }
}