package eu.peernetwork.social.data.api

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.domain.model.Block

interface ModerationApi {
    suspend fun get(userId: String, pageable: Pageable): Page<Block>

    suspend fun block(userId: String): Boolean

    suspend fun report(userId: String): Boolean
}