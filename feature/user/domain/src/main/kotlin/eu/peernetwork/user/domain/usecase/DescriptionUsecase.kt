package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.user.domain.repository.AccountRepository
import eu.peernetwork.user.domain.repository.ResourceRepository
import javax.inject.Inject

class DescriptionUsecase @Inject constructor(
    private val repository: ResourceRepository
) : ParameterizedSuspendableUseCase<String, String>{
    override suspend fun invoke(param: String): String {
        return repository.string(param)
    }
}
