package eu.peernetwork.wallet.data.repository

import eu.peernetwork.wallet.data.api.RewardApi
import eu.peernetwork.wallet.domain.model.Reward
import eu.peernetwork.wallet.domain.repository.RewardRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class RewardRepositoryDelegate @Inject constructor(
    private val api: RewardApi
) : RewardRepository {
    private val mutableState = MutableSharedFlow<List<Reward>>(replay = 1)

    init { mutableState.tryEmit(emptyList()) }

    override suspend fun get(): List<Reward> {
        val rewards = api.get()
        mutableState.tryEmit(rewards)
        return rewards
    }

    override fun observe(): SharedFlow<List<Reward>> = mutableState
}
