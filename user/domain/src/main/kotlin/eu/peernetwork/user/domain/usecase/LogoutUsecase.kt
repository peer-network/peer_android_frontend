package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.interactor.AuthenticationInteractor
import javax.inject.Inject

class LogoutUsecase @Inject constructor(
    private val interactor: AuthenticationInteractor
) : SuspendableUseCase<Unit> {
    override suspend fun invoke() {
        return interactor.logout()
    }
}
