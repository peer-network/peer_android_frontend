package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.usecase.ObservableUseCase
import eu.peernetwork.social.domain.interactor.ConnectionInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveConnectionsUsecase @Inject constructor(
    private val interactor: ConnectionInteractor
) : ObservableUseCase<Map<String, Boolean>> {
    override fun invoke(): Flow<Map<String, Boolean>> {
        return interactor.observe()
    }
}
