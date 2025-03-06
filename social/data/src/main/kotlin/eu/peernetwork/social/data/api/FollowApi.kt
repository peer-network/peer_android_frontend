package eu.peernetwork.social.data.api

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.User
import kotlinx.coroutines.flow.SharedFlow

interface FollowApi {
    suspend fun follow(id: String, enable: Boolean)

    fun followers(id: String, pageable: Pageable): SharedFlow<List<User>>

    fun following(id: String, pageable: Pageable): SharedFlow<List<User>>
}
