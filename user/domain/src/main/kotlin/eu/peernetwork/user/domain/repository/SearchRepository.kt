package eu.peernetwork.user.domain.repository

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.user.domain.model.Account
import kotlinx.coroutines.flow.SharedFlow

interface SearchRepository {
    fun users(filter: Map<String, Any>, pageable: Pageable): SharedFlow<List<Account>>
}
