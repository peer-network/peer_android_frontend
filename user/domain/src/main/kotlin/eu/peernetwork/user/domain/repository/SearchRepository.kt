package eu.peernetwork.user.domain.repository

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.user.domain.model.User

interface SearchRepository {
    suspend fun filterByUsername(username: String, pageable: Pageable): Page<User>
}
