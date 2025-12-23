package eu.peernetwork.wallet.data.api

import eu.peernetwork.wallet.domain.model.Tax

interface TaxApi {
    suspend fun getTax(): Tax
}
