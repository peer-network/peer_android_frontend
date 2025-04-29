package eu.peernetwork.user.domain.repository

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.user.domain.model.User

interface SearchRepository {
    suspend fun filterByUsername(username: String, pageable: Pageable): Page<User>
}
