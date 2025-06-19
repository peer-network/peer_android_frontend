package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.repository.BlockRepository
import javax.inject.Inject

class BlockUsecase @Inject constructor(
    private val repository: BlockRepository
): ParameterizedSuspendableUseCase<String, Boolean> {
    override suspend fun invoke(param: String): Boolean {
        return repository.block(param)
    }
}