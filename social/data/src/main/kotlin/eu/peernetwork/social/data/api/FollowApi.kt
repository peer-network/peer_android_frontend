package eu.peernetwork.social.data.api

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Member

interface FollowApi {
    suspend fun follow(id: String): Boolean

    suspend fun followers(id: String, pageable: Pageable): Page<Member>

    suspend fun following(id: String, pageable: Pageable): Page<Member>

    suspend fun friends(id: String, pageable: Pageable): Page<Member>
}
