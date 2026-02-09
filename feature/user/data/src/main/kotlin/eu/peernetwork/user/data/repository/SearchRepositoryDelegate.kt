package eu.peernetwork.user.data.repository

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.user.data.api.SearchApi
import eu.peernetwork.user.domain.model.User
import eu.peernetwork.user.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryDelegate @Inject constructor(
    private val api: SearchApi
) : SearchRepository {
    override suspend fun filterByUsername(username: String, pageable: Pageable): Page<User> {
        return api.findByUsername(username, pageable)
    }
}
