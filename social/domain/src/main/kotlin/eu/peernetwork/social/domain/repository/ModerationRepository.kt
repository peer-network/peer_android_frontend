package eu.peernetwork.social.domain.repository

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.domain.model.Block

interface ModerationRepository {
    suspend fun get(userId: String, pageable: Pageable): Page<Block>

    suspend fun block(userId: String): Boolean

    suspend fun report(userId: String): Boolean
}
