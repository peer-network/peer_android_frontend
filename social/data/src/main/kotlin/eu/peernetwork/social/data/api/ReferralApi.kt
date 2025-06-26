package eu.peernetwork.social.data.api

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Referral

interface ReferralApi {
    suspend fun get(userId: String, pageable: Pageable): Page<Referral>
}