package eu.peernetwork.wallet.remote.api

import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.wallet.data.api.RewardApi
import eu.peernetwork.wallet.domain.model.Reward
import wallet.wallet.eu.peernetwork.wallet.remote.DailyRewardsQuery
import javax.inject.Inject

class RewardApiDelegate @Inject constructor(
    private val client: RequestClient
) : RewardApi {
    override suspend fun get(): List<Reward> {
        val response = client().query(DailyRewardsQuery()).executeOrThrow()
        val data = response.getOrThrow().getDailyFreeStatus
        response.assertOrThrow(data.status, data.ResponseCode)
        return data.affectedRows?.mapNotNull {
            Reward(
                type = it!!.name,
                used = it.used,
                available = it.available
            )
        } ?: emptyList()
    }
}
