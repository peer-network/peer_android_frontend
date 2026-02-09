package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.model.User
import eu.peernetwork.user.domain.repository.ReferralRepository
import javax.inject.Inject

class ReferralVerificationUsecase @Inject constructor(
    private val repository: ReferralRepository
) : ParameterizedSuspendableUseCase<String, User.Profile> {
    override suspend fun invoke(param: String): User.Profile {
        return repository.get(param)
    }
}
