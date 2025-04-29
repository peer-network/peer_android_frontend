package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.repository.FollowRepository
import javax.inject.Inject

class FollowUsecase @Inject constructor(
    private val repository: FollowRepository
) : ParameterizedSuspendableUseCase<String, Boolean> {
    override suspend fun invoke(param: String): Boolean{
        return repository.follow(param)
    }
}