package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendUseCase
import eu.peernetwork.user.domain.model.AccountDetail
import eu.peernetwork.user.domain.repository.AccountRepository
import javax.inject.Inject

class RegistrationUsecase @Inject constructor(
    private val repository: AccountRepository
) : ParameterizedSuspendUseCase<RegistrationUsecase.Parameter, String> {
    override suspend fun invoke(param: Parameter): String {
        return repository.register(
            AccountDetail(
                email = param.email,
                username = param.username,
                password = param.password
            )
        )
    }

    data class Parameter(
        val email: String,
        val username: String,
        val password: String,
    )
}
