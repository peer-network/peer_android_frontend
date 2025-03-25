package eu.peernetwork.user.domain.repository

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.user.domain.model.User
import kotlinx.coroutines.flow.SharedFlow

interface SearchRepository {
    fun observe(): SharedFlow<List<User>>

    suspend fun filterByUsername(username: String, pageable: Pageable)
}
