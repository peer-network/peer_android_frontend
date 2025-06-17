package eu.peernetwork.wallet.domain.repository

import eu.peernetwork.wallet.domain.model.Tax

interface TaxRepository {
    suspend fun getTax(): Tax
}
