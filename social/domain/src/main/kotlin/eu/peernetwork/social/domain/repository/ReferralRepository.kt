package eu.peernetwork.social.domain.repository

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Referral

interface ReferralRepository {
    suspend fun getAll(userId: String, pageable: Pageable): Page<Referral>
}
