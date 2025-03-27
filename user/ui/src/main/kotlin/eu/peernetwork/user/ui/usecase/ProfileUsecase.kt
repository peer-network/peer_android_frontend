package eu.peernetwork.user.ui.usecase

import eu.peernetwork.core.common.provider.DispatcherProvider
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.usecase.AuthUserUsecase
import eu.peernetwork.user.ui.model.UiAccount
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileUsecase @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val usecase: AuthUserUsecase,
    private val userUsecase: UserUsecase,
) : SuspendableUseCase<UiAccount> {
    override suspend fun invoke(): UiAccount {
        return withContext(dispatcher.io) {
            userUsecase(usecase())
        }
    }
}
