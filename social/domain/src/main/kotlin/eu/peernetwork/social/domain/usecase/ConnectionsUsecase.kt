package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.interactor.ConnectionInteractor
import javax.inject.Inject

class ConnectionsUsecase @Inject constructor(
    private val interactor: ConnectionInteractor
) : ParameterizedSuspendableUseCase<String, Unit> {
    override suspend fun invoke(param: String) {
        return interactor.connect(param)
    }
}
