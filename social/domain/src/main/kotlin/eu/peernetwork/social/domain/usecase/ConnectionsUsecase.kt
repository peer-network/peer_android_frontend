package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.interactor.ConnectionInteractor
import javax.inject.Inject

class ConnectionsUsecase @Inject constructor(
    private val interactor: ConnectionInteractor
) : ParameterizedSuspendableUseCase<ConnectionsUsecase.Parameter, Unit> {
    override suspend fun invoke(param: Parameter) {
        return interactor.connect(param.id, param.value)
    }

    data class Parameter(
        val id: String,
        val value: Boolean = true
    )
}
