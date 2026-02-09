package eu.peernetwork.wallet.remote.api

import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.wallet.data.api.ReferralApi
import wallet.wallet.eu.peernetwork.wallet.remote.GetReferralQuery
import javax.inject.Inject

class ReferralApiDelegate @Inject constructor(
    private val client: RequestClient
) : ReferralApi {
    override suspend fun get(): String? {
        val response = client().query(GetReferralQuery()).execute()
        val data = response.getOrThrow().getUserInfo
        response.assertOrThrow(data.status, data.ResponseCode)
        return data.affectedRows?.invited
    }
}
