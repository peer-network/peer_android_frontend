package eu.peernetwork.social.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.social.data.api.FollowApi
import eu.peernetwork.social.domain.model.Member
import social.social.eu.peernetwork.social.remote.UserFollowMutation
import javax.inject.Inject

class FollowApiDelegate @Inject constructor(private val client: ApolloClient) : FollowApi {
    override suspend fun follow(id: String): Boolean {
        val mutation = UserFollowMutation(id)
        val response = client.mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().toggleUserFollowStatus
        response.assertOrThrow(data.status, data.ResponseCode)
        return data.isfollowing == true
    }

    override suspend fun followers(
        id: String,
        pageable: Pageable
    ): Page<Member> {
        TODO("Not yet implemented")
    }

    override suspend fun following(
        id: String,
        pageable: Pageable
    ): Page<Member> {
        TODO("Not yet implemented")
    }

    override suspend fun friends(
        id: String,
        pageable: Pageable
    ): Page<Member> {
        TODO("Not yet implemented")
    }
}
