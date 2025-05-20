package eu.peernetwork.social.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.core.remote.provider.NetworkProvider
import eu.peernetwork.social.data.api.FollowApi
import eu.peernetwork.social.domain.model.Member
import social.social.eu.peernetwork.social.remote.ListFollowRelationsQuery
import social.social.eu.peernetwork.social.remote.ListFollowingsRelationsQuery
import social.social.eu.peernetwork.social.remote.ListPeersQuery
import social.social.eu.peernetwork.social.remote.UserFollowMutation
import javax.inject.Inject
import javax.inject.Named

class FollowApiDelegate @Inject constructor(
    @Named("mediaUrl") private val url: String,
    private val provider: NetworkProvider
) : FollowApi {
    override suspend fun follow(id: String): Boolean {
        val mutation = UserFollowMutation(id)
        val response = provider.client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().toggleUserFollowStatus
        response.assertOrThrow(data.status, data.ResponseCode)
        return data.isfollowing == true
    }

    override suspend fun followers(
        id: String,
        pageable: Pageable
    ): Page<Member> {
        val query = ListFollowRelationsQuery(
            userid = id,
            offset = pageable.offset,
            limit = pageable.limit
        )
        val response = provider.client().query(query).executeOrThrow()
        val data = response.getOrThrow().listFollowRelations
        response.assertOrThrow(data.status, data.ResponseCode)
        val followers = data.affectedRows?.followers?.map {
            Member(
                id = it.id,
                username = it.username!!,
                imageUrl = "$url/${it.img!!}".removeSuffix("/")
            )
        }
        return Page(
            count = data.counter,
            items = followers ?: emptyList(),
            offset = pageable.offset,
        )
    }

    override suspend fun following(
        id: String,
        pageable: Pageable
    ): Page<Member> {
        val query = ListFollowingsRelationsQuery(
            userid = id,
            offset = pageable.offset,
            limit = pageable.limit
        )
        val response = provider.client().query(query).executeOrThrow()
        val data = response.getOrThrow().listFollowRelations
        response.assertOrThrow(data.status, data.ResponseCode)
        val following = data.affectedRows?.following?.map {
            Member(
                id = it.id,
                username = it.username!!,
                imageUrl = "$url/${it.img!!}".removeSuffix("/")
            )
        }
        return Page(
            count = data.counter,
            items = following ?: emptyList(),
            offset = pageable.offset,
        )
    }

    override suspend fun friends(
        pageable: Pageable
    ): Page<Member> {
        val query = ListPeersQuery(
            offset = pageable.offset,
            limit = pageable.limit
        )
        val response = provider.client().query(query).executeOrThrow()
        val data = response.getOrThrow().listFriends
        response.assertOrThrow(data.status, data.ResponseCode)
        val friends = data.affectedRows?.map {
            Member(
                id = it?.userid ?: "",
                username = it?.username ?: "Unknown",
                imageUrl = "$url/${it?.img!!}".removeSuffix("/")
            )
        }
        return Page(
            count = data.counter,
            items = friends ?: emptyList(),
            offset = pageable.offset,
        )
    }
}
