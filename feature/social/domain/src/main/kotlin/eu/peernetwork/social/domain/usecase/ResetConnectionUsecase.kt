package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.usecase.BlockingUseCase
import eu.peernetwork.social.domain.interactor.ConnectionInteractor
import javax.inject.Inject

class ResetConnectionUsecase @Inject constructor(
    private val interactor: ConnectionInteractor
) : BlockingUseCase<Unit> {
    override fun invoke() {
        return interactor.clear()
    }
}
