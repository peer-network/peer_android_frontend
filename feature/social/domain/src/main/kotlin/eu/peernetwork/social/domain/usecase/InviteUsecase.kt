package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.social.domain.model.Invite
import eu.peernetwork.social.domain.repository.InviteRepository
import javax.inject.Inject

class InviteUsecase @Inject constructor(
    private val repository: InviteRepository
): SuspendableUseCase<Invite> {
    override suspend fun invoke(): Invite {
        return repository.get()
    }
}