package eu.peernetwork.social.remote.api

import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.social.data.api.InviteApi
import eu.peernetwork.social.domain.model.Invite
import social.social.eu.peernetwork.social.remote.GetReferralInfoQuery
import javax.inject.Inject
import javax.inject.Named

class InviteApiDelegate @Inject constructor(
    @Named("mediaUrl") private val url: String,
    private val client: RequestClient
): InviteApi {
    override suspend fun get(): Invite {
        val query = GetReferralInfoQuery()
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().getReferralInfo
        response.assertOrThrow(data.status, data.ResponseCode)
        return Invite(
            id = data.referralUuid!!,
            link = data.referralLink!!
        )
    }
}