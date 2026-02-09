package eu.peernetwork.social.data.repository

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.data.api.ModerationApi
import eu.peernetwork.social.domain.model.Block
import eu.peernetwork.social.domain.repository.ModerationRepository
import javax.inject.Inject

class ModerationRepositoryDelegate @Inject constructor(
    private val api: ModerationApi
): ModerationRepository {
    override suspend fun get(userId: String, pageable: Pageable): Page<Block> {
        return api.get(userId, pageable)
    }

    override suspend fun block(userId: String): Boolean {
        return api.block(userId)
    }

    override suspend fun report(userId: String): String {
        return api.report(userId)
    }
}