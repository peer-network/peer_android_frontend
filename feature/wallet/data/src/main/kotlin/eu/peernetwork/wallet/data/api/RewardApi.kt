package eu.peernetwork.wallet.data.api

import eu.peernetwork.wallet.domain.model.Reward

interface RewardApi {
    suspend fun get(): List<Reward>
}
