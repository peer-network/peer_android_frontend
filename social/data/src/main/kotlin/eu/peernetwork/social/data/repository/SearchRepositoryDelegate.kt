package eu.peernetwork.social.data.repository

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.User
import eu.peernetwork.social.domain.repository.SearchRepository
import kotlinx.coroutines.flow.SharedFlow

class SearchRepositoryDelegate : SearchRepository {
    override fun friends(id: String, pageable: Pageable): SharedFlow<List<User>> {
        TODO("Not yet implemented")
    }
}
