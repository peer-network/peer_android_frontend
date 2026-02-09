package eu.peernetwork.wallet.domain.repository

import eu.peernetwork.wallet.domain.model.Reward
import kotlinx.coroutines.flow.SharedFlow

interface RewardRepository {
    suspend fun get(): List<Reward>

    fun observe(): SharedFlow<List<Reward>>
}
