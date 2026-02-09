package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.repository.ReferralRepository
import javax.inject.Inject

class ReferralUsecase @Inject constructor(
    private val repository: ReferralRepository
) : SuspendableUseCase<String> {
    override suspend fun invoke(): String {
        return repository.get()
    }
}
