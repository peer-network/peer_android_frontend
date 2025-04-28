package eu.peernetwork.social.data.repository

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.data.api.FollowApi
import eu.peernetwork.social.domain.model.User
import eu.peernetwork.social.domain.repository.FollowRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class FollowRepositoryDelegate @Inject constructor(
    private val api: FollowApi
) : FollowRepository {
    override suspend fun follow(id: String): Boolean {
        return api.follow(id)
    }

    override fun followers(id: String, pageable: Pageable): SharedFlow<List<User>> {
        TODO("Not yet implemented")
    }

    override fun following(id: String, pageable: Pageable): SharedFlow<List<User>> {
        TODO("Not yet implemented")
    }
}
