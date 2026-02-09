package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.user.domain.repository.AuthenticationRepository
import javax.inject.Inject

class LoginUsecase @Inject constructor(
    private val repository: AuthenticationRepository
) : ParameterizedSuspendableUseCase<LoginUsecase.Parameter, String>{
    override suspend fun invoke(param: Parameter): String {
        return repository.login(
            email = param.email,
            password = param.password,
            remember = param.rememberMe
        )
    }

    data class Parameter(
        val email: String,
        val password: String,
        val rememberMe: Boolean = false,
    )
}
