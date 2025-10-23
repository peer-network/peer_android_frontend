package eu.peernetwork.user.remote.api

import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.user.data.api.ReferralApi
import eu.peernetwork.user.domain.exception.ResourceNotFoundException
import eu.peernetwork.user.domain.model.User
import public.eu.peernetwork.user.remote.PeerReferralQuery
import public.eu.peernetwork.user.remote.VerifyReferralStringMutation
import javax.inject.Inject

class ReferralApiDelegate @Inject constructor(
    private val client: RequestClient
) : ReferralApi {
    override suspend fun get(): String {
        val query = PeerReferralQuery()
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().hello
        return data?.companyAccountId ?: throw ResourceNotFoundException()
    }

    override suspend fun get(code: String): User.Profile {
        val mutation = VerifyReferralStringMutation(referralString = code)
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().verifyReferralString
        response.assertOrThrow(data.status, data.ResponseCode)
        return data.affectedRows?.let {
            User.Profile(
                id = it.uid!!,
                username = it.username!!,
                slug = it.slug!!,
                imageUrl = it.img!!
            )
        } ?: throw ResourceNotFoundException()
    }
}
