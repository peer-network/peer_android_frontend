package eu.peernetwork.user.data.repository

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.user.data.api.SearchApi
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.repository.SearchRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class SearchRepositoryDelegate @Inject constructor(
    private val api: SearchApi
) : SearchRepository {
    override fun findByUsername(username: String, pageable: Pageable): SharedFlow<List<Account>> {
        TODO("Not yet implemented")
    }
}
