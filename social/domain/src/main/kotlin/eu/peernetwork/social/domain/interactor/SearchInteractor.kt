package eu.peernetwork.social.domain.interactor

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.domain.model.Member

interface SearchInteractor {
    suspend fun findMember(username: String, pageable: Pageable): Page<Member>
}
