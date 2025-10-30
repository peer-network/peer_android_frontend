package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.model.Invite
import eu.peernetwork.user.domain.repository.ReferralRepository
import javax.inject.Inject

class InvitationUsecase @Inject constructor(
    private val repository: ReferralRepository
) : SuspendableUseCase<Invite> {
    override suspend fun invoke(): Invite {
        return repository.invitation()
    }
}
