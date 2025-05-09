package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.usecase.ImmediateUseCase
import eu.peernetwork.social.domain.interactor.ConnectionInteractor
import javax.inject.Inject

class ResetConnectionUsecase @Inject constructor(
    private val interactor: ConnectionInteractor
) : ImmediateUseCase<Unit> {
    override fun invoke() {
        return interactor.clear()
    }
}
