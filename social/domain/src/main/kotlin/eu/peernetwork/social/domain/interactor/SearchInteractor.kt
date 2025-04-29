package eu.peernetwork.social.domain.interactor

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Content
import eu.peernetwork.social.domain.model.Member

interface SearchInteractor {
    suspend fun tag(username: String, pageable: Pageable): Page<Content>

    suspend fun title(username: String, pageable: Pageable): Page<Content>

    suspend fun user(username: String, pageable: Pageable): Page<Member>
}
