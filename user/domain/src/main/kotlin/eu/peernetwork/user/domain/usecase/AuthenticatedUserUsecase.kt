package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.model.Account
import javax.inject.Inject

class AuthenticatedUserUsecase @Inject constructor(
    private val principalUsecase: PrincipalUsecase,
    private val profileUsecase: ProfileUsecase
) : SuspendableUseCase<Account> {
    override suspend fun invoke(): Account {
        return profileUsecase(principalUsecase())
    }
}
