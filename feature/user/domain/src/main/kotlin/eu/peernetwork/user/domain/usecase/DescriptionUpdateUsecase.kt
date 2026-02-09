package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.user.domain.repository.AccountRepository
import javax.inject.Inject

class DescriptionUpdateUsecase @Inject constructor(
    private val repository: AccountRepository
) : ParameterizedSuspendableUseCase<Pair<String, String>, Unit>{
    override suspend fun invoke(param: Pair<String, String>) {
        return repository.update(mapOf(param))
    }
}
