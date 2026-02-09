package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.user.domain.repository.AccountRepository
import javax.inject.Inject

class PasswordResetUsecase @Inject constructor(
    private val repository: AccountRepository
) : ParameterizedSuspendableUseCase<PasswordResetUsecase.Parameter, Unit> {
    override suspend fun invoke(param: Parameter) {
        return repository.resetPassword(param.token, param.password)
    }

    data class Parameter(
        val token: String,
        val password: String
    )
}
