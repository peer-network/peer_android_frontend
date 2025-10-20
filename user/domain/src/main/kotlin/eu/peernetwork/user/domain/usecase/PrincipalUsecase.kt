package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.interactor.AuthenticationInteractor
import javax.inject.Inject

class PrincipalUsecase @Inject constructor(
    private val interactor: AuthenticationInteractor
) : ParameterizedSuspendableUseCase<Boolean, String> {
    override suspend fun invoke(param: Boolean): String {
        return if (param) {
            interactor.getCurrentAccount(true).id
        } else {
            interactor.get()
        }
    }
}
