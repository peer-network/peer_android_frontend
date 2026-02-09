package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.interactor.AuthenticationInteractor
import eu.peernetwork.user.domain.model.Account
import javax.inject.Inject

class AuthRefreshUsecase @Inject constructor(
    private val interactor: AuthenticationInteractor
) : SuspendableUseCase<Account> {
    override suspend fun invoke(): Account {
        return interactor.getCurrentAccount(true)
    }
}
