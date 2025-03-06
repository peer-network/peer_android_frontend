package eu.peernetwork.user.data.repository

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.user.data.api.SearchApi
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.repository.SearchRepository
import kotlinx.coroutines.flow.SharedFlow

class SearchRepositoryDelegate(
    private val api: SearchApi
) : SearchRepository {
    override fun users(filter: Map<String, Any>, pageable: Pageable): SharedFlow<List<Account>> {
        return api.users(filter, pageable)
    }
}
