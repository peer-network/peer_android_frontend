package eu.peernetwork.social.data.api

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.domain.model.Referral

interface ReferralApi {
    suspend fun get(userId: String, pageable: Pageable): Page<Referral>
}