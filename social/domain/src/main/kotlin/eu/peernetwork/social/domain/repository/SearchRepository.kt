package eu.peernetwork.social.domain.repository

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.User
import kotlinx.coroutines.flow.SharedFlow

interface SearchRepository {
    fun friends(id: String, pageable: Pageable): SharedFlow<List<User>>
}
