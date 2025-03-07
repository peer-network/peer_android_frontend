package eu.peernetwork.user.domain.repository

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.user.domain.model.Account
import kotlinx.coroutines.flow.SharedFlow

interface SearchRepository {
    fun findByUsername(username: String, pageable: Pageable): SharedFlow<List<Account>>
}
