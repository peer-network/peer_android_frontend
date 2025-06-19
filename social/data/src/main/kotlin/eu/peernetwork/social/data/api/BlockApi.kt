package eu.peernetwork.social.data.api

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Block

interface BlockApi {
    suspend fun get(userId: String, pageable: Pageable): Page<Block>

    suspend fun block(userId: String): Boolean
}