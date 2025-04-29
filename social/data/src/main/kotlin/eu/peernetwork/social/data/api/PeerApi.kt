package eu.peernetwork.social.data.api

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Member

interface PeerApi {
    suspend fun friends(id: String, pageable: Pageable): Page<Member>
}
