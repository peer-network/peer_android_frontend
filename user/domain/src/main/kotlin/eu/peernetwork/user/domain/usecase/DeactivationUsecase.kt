package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.user.domain.repository.AccountRepository
import javax.inject.Inject

class DeactivationUsecase @Inject constructor(
    private val repository: AccountRepository,
    private val logoutUsecase: LogoutUsecase
) : ParameterizedSuspendableUseCase<String, Unit>{
    override suspend fun invoke(param: String) {
        repository.delete(param)
        logoutUsecase()
    }
}
