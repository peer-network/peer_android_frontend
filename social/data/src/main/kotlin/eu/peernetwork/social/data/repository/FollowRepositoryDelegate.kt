package eu.peernetwork.social.data.repository

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.User
import eu.peernetwork.social.domain.repository.FollowRepository
import kotlinx.coroutines.flow.SharedFlow

class FollowRepositoryDelegate : FollowRepository {
    override suspend fun follow(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun unfollow(id: String) {
        TODO("Not yet implemented")
    }

    override fun followers(id: String, pageable: Pageable): SharedFlow<List<User>> {
        TODO("Not yet implemented")
    }

    override fun following(id: String, pageable: Pageable): SharedFlow<List<User>> {
        TODO("Not yet implemented")
    }
}
