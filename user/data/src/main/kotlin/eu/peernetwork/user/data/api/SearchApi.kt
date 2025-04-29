package eu.peernetwork.user.data.api

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.user.domain.model.User

interface SearchApi {
    suspend fun findByUsername(username: String, pageable: Pageable): Page<User>
}
