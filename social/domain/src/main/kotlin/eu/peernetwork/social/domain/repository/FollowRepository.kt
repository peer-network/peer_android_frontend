package eu.peernetwork.social.domain.repository

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Member
import kotlinx.coroutines.flow.SharedFlow

interface FollowRepository {
    suspend fun follow(id: String): Boolean

    fun followers(id: String, pageable: Pageable): SharedFlow<List<Member>>

    fun following(id: String, pageable: Pageable): SharedFlow<List<Member>>
}
