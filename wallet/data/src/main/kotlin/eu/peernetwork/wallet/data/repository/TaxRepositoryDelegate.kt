package eu.peernetwork.wallet.data.repository

import eu.peernetwork.wallet.data.api.ReferralApi
import eu.peernetwork.wallet.domain.model.Tax
import eu.peernetwork.wallet.domain.repository.TaxRepository
import javax.inject.Inject

class TaxRepositoryDelegate @Inject constructor(
    private val api: ReferralApi
) : TaxRepository {
    override suspend fun getTax(): Tax {
        val referral = api.get()
        if (referral == null || referral.isEmpty()) {
            return Tax(4.0)
        }
        return Tax(5.0)
    }
}
