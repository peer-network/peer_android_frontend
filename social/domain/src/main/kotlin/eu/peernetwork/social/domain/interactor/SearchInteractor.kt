package eu.peernetwork.social.domain.interactor

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Member

interface SearchInteractor {
    suspend fun findMember(username: String, pageable: Pageable): Page<Member>
}
