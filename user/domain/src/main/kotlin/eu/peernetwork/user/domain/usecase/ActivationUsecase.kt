package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendUseCase
import eu.peernetwork.user.domain.repository.AccountRepository
import javax.inject.Inject

class ActivationUsecase @Inject constructor(
    private val repository: AccountRepository
) : ParameterizedSuspendUseCase<String, Unit>{
    override suspend fun invoke(param: String) {
        return repository.activate(param)
    }
}
