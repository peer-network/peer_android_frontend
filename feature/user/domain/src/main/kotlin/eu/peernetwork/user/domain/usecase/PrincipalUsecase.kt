package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.user.domain.interactor.AuthenticationInteractor
import eu.peernetwork.user.domain.model.Account
import javax.inject.Inject

class PrincipalUsecase @Inject constructor(
    private val interactor: AuthenticationInteractor
) : ParameterizedSuspendableUseCase<Boolean, Account> {
    override suspend fun invoke(param: Boolean): Account {
        return interactor.getCurrentAccount(param)
    }
}
