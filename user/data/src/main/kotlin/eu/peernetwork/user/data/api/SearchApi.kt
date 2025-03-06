package eu.peernetwork.user.data.api

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.user.domain.model.Account
import kotlinx.coroutines.flow.SharedFlow

interface SearchApi {
    fun users(filter: Map<String, Any>, pageable: Pageable): SharedFlow<List<Account>>
}
