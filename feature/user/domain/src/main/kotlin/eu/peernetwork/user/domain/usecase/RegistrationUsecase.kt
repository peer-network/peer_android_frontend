package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.user.domain.model.UserDetail
import eu.peernetwork.user.domain.repository.AccountRepository
import javax.inject.Inject

class RegistrationUsecase @Inject constructor(
    private val repository: AccountRepository
) : ParameterizedSuspendableUseCase<RegistrationUsecase.Parameter, String> {
    override suspend fun invoke(param: Parameter): String {
        return repository.register(
            UserDetail(
                email = param.email,
                username = param.username,
                password = param.password
            ),
            param.referral
        )
    }

    data class Parameter(
        val email: String,
        val username: String,
        val password: String,
        val referral: String?
    )
}
