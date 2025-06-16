package eu.peernetwork.wallet.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.wallet.domain.model.Reward
import eu.peernetwork.wallet.domain.repository.RewardRepository
import javax.inject.Inject

class RewardUsecase @Inject constructor(
    private val repository: RewardRepository
) : SuspendableUseCase<List<Reward>> {
    override suspend fun invoke(): List<Reward> {
        return repository.get()
    }
}
