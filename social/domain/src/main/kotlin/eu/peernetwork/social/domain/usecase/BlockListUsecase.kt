package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.model.Block
import eu.peernetwork.social.domain.repository.ModerationRepository
import javax.inject.Inject

class BlockListUsecase @Inject constructor(
    private val repository: ModerationRepository
): ParameterizedSuspendableUseCase<BlockListUsecase.Params, Page<Block>> {
    override suspend fun invoke(param: Params): Page<Block> {
        return repository.get(param.userId, param.pageable)
    }

    data class Params(
        val userId: String,
        val pageable: Pageable
    )
}