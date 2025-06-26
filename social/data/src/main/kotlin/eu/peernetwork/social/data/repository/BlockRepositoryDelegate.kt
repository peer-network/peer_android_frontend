package eu.peernetwork.social.data.repository

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.data.api.BlockApi
import eu.peernetwork.social.domain.model.Block
import eu.peernetwork.social.domain.repository.BlockRepository
import javax.inject.Inject

class BlockRepositoryDelegate @Inject constructor(
    private val api: BlockApi
): BlockRepository {
    override suspend fun get(userId: String, pageable: Pageable): Page<Block> {
        return api.get(userId, pageable)
    }

    override suspend fun block(userId: String): Boolean {
        return api.block(userId)
    }
}