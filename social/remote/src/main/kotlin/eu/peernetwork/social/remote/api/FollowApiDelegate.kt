package eu.peernetwork.social.remote.api

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.data.api.FollowApi
import eu.peernetwork.social.domain.model.User
import kotlinx.coroutines.flow.SharedFlow

class FollowApiDelegate : FollowApi {
    override suspend fun follow(id: String, enable: Boolean) {
        TODO("Not yet implemented")
    }

    override fun followers(id: String, pageable: Pageable): SharedFlow<List<User>> {
        TODO("Not yet implemented")
    }

    override fun following(id: String, pageable: Pageable): SharedFlow<List<User>> {
        TODO("Not yet implemented")
    }
}
