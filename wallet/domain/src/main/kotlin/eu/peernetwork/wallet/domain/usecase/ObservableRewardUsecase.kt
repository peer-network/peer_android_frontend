package eu.peernetwork.wallet.domain.usecase

import eu.peernetwork.core.common.usecase.ObservableUseCase
import eu.peernetwork.wallet.domain.model.Reward
import eu.peernetwork.wallet.domain.repository.RewardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservableRewardUsecase @Inject constructor(
    private val repository: RewardRepository
) : ObservableUseCase<List<Reward>> {
    override fun invoke(): Flow<List<Reward>> {
        return repository.observe()
    }
}
