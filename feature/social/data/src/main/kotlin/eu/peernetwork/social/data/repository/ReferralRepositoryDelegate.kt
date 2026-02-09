package eu.peernetwork.social.data.repository

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.data.api.ReferralApi
import eu.peernetwork.social.domain.model.Referral
import eu.peernetwork.social.domain.repository.ReferralRepository
import javax.inject.Inject

class ReferralRepositoryDelegate @Inject constructor(
    private val api: ReferralApi
): ReferralRepository {
    override suspend fun getAll(
        userId: String,
        pageable: Pageable
    ): Page<Referral> {
        return api.get(userId, pageable)
    }
}