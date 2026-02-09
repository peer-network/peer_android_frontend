package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.user.domain.repository.AccountRepository
import javax.inject.Inject

class PasswordChangeUsecase @Inject constructor(
    private val repository: AccountRepository
) : ParameterizedSuspendableUseCase<PasswordChangeUsecase.Parameter, Unit> {
    override suspend fun invoke(param: Parameter) {
        return repository.changePassword(param.current, param.new)
    }

    data class Parameter(
        val current: String,
        val new: String,
    )
}
