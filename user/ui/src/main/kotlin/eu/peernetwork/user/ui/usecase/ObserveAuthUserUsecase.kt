package eu.peernetwork.user.ui.usecase

import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.ObservableUseCase
import eu.peernetwork.user.domain.interactor.AuthenticationInteractor
import eu.peernetwork.user.ui.model.UiAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ObserveAuthUserUsecase @Inject constructor(
    private val dispatcher: Dispatcher,
    private val interactor: AuthenticationInteractor,
    private val userUsecase: UserUsecase,
) : ObservableUseCase<UiAccount?> {
    override fun invoke(): Flow<UiAccount?> {
        return interactor.observeAccount().map { account ->
            withContext(dispatcher.io) {
                account?.let { userUsecase(it) }
            }
        }
    }
}
