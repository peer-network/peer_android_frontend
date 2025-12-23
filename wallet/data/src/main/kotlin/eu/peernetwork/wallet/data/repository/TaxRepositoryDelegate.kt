package eu.peernetwork.wallet.data.repository

import eu.peernetwork.wallet.data.api.ReferralApi
import eu.peernetwork.wallet.data.api.TaxApi
import eu.peernetwork.wallet.domain.model.Tax
import eu.peernetwork.wallet.domain.repository.TaxRepository
import javax.inject.Inject

class TaxRepositoryDelegate @Inject constructor(
    private val api: ReferralApi,
    private val taxApi: TaxApi
) : TaxRepository {
    override suspend fun getTax(): Tax {
        val referral = api.get()
        if (referral == null || referral.isEmpty()) {
            return taxApi.getTax().copy(percentage = 0.0)
        }
        return taxApi.getTax()
    }
}
