package eu.peernetwork.app.usecase

import eu.peernetwork.app.interceptor.SubscriptionInteractor
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class UnSubscriptionUsecase @Inject constructor(
    private val interactor: SubscriptionInteractor
) : ParameterizedSuspendableUseCase<String, Unit> {
    override suspend fun invoke(param: String) {
        interactor.unSubscribe()
    }
}
