package eu.peernetwork.social.domain.repository

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.User
import kotlinx.coroutines.flow.SharedFlow

interface FollowRepository {
    suspend fun follow(id: String)

    suspend fun unfollow(id: String)

    fun followers(id: String, pageable: Pageable): SharedFlow<List<User>>

    fun following(id: String, pageable: Pageable): SharedFlow<List<User>>
}
