package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ObservableUseCase
import eu.peernetwork.user.domain.interactor.AuthenticationInteractor
import eu.peernetwork.user.domain.model.Account
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthUserObserverUsecase @Inject constructor(
    private val interactor: AuthenticationInteractor
) : ObservableUseCase<Account?> {
    override fun invoke(): Flow<Account?> {
        return interactor.observeAccount()
    }
}
