package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.user.domain.repository.AccountRepository
import javax.inject.Inject

class ProtectedSettingsUsecase @Inject constructor(
    private val repository: AccountRepository
) : ParameterizedSuspendableUseCase<ProtectedSettingsUsecase.Parameter, Unit>{
    override suspend fun invoke(param: Parameter) {
        return repository.update(mapOf(param.name to param.value), param.password)
    }

    data class Parameter(
        val name: String,
        val value: Any,
        val password: String
    )
}
