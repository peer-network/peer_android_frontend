package eu.peernetwork.user.ui.usecase

import eu.peernetwork.core.common.concurrent.Dispatcher
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.usecase.AuthRefreshUsecase
import eu.peernetwork.user.ui.model.UiAccount
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRefreshUsecase @Inject constructor(
    private val dispatcher: Dispatcher,
    private val usecase: AuthRefreshUsecase,
    private val userUsecase: UserUsecase,
) : SuspendableUseCase<UiAccount> {
    override suspend fun invoke(): UiAccount {
        return withContext(dispatcher.io) {
            userUsecase(usecase())
        }
    }
}
