package eu.peernetwork.social.domain.repository

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Member

interface SearchRepository {
    suspend fun friends(id: String, pageable: Pageable): Page<Member>
}
