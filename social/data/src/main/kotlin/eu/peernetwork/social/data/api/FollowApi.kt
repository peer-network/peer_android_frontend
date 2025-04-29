package eu.peernetwork.social.data.api

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Member
import kotlinx.coroutines.flow.SharedFlow

interface FollowApi {
    suspend fun follow(id: String): Boolean

    fun followers(id: String, pageable: Pageable): SharedFlow<List<Member>>

    fun following(id: String, pageable: Pageable): SharedFlow<List<Member>>
}
