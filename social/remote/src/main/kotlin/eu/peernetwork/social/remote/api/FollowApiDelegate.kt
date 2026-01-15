package eu.peernetwork.social.remote.api

import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.common.interactor.SessionInteractor
import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.social.data.api.FollowApi
import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.social.remote.mapper.mapToDomain
import eu.peernetwork.social.remote.mapper.mapToMode
import social.social.eu.peernetwork.social.remote.ListFollowRelationsQuery
import social.social.eu.peernetwork.social.remote.ListFollowingsRelationsQuery
import social.social.eu.peernetwork.social.remote.ListPeersQuery
import social.social.eu.peernetwork.social.remote.UserFollowMutation
import javax.inject.Inject
import javax.inject.Named

class FollowApiDelegate @Inject constructor(
    @Named("mediaUrl") private val url: String,
    private val client: RequestClient,
    private val sessionInteractor: SessionInteractor
) : FollowApi {
    override suspend fun follow(id: String): Boolean {
        val mutation = UserFollowMutation(id)
        val response = client().mutation(mutation).executeOrThrow()
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
            contentFilterBy = Optional.present(sessionInteractor.mode().mapToMode()),
            offset = pageable.offset,
            limit = pageable.limit
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().listFollowRelations
        response.assertOrThrow(data.status, data.ResponseCode)
        val followers = data.affectedRows?.followers?.map {
            Member(
                id = it.id,
                slug = it.slug!!.toString(),
                username = it.username!!,
                imageUrl = "$url/${it.img!!}".removeSuffix("/"),
                isFollowing = it.isfollowing ?: false,
                isFollowed = it.isfollowed ?: false,
                isAccessible = !it.isHiddenForUsers,
                status = it.visibilityStatus.mapToDomain()
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
            contentFilterBy = Optional.present(sessionInteractor.mode().mapToMode()),
            offset = pageable.offset,
            limit = pageable.limit
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().listFollowRelations
        response.assertOrThrow(data.status, data.ResponseCode)
        val following = data.affectedRows?.following?.map {
            Member(
                id = it.id,
                slug = it.slug!!.toString(),
                username = it.username!!,
                imageUrl = "$url/${it.img!!}".removeSuffix("/"),
                isFollowing = it.isfollowing ?: false,
                isFollowed = it.isfollowed ?: false,
                isAccessible = !it.isHiddenForUsers,
                status = it.visibilityStatus.mapToDomain()
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
            contentFilterBy = Optional.present(sessionInteractor.mode().mapToMode()),
            offset = pageable.offset,
            limit = pageable.limit
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().listFriends
        response.assertOrThrow(data.status, data.ResponseCode)
        val friends = data.affectedRows?.map {
            Member(
                id = it?.userid ?: "",
                slug = it?.slug!!.toString(),
                username = it.username ?: "Unknown",
                imageUrl = "$url/${it.img!!}".removeSuffix("/"),
                isFollowing = true,
                isFollowed = true,
                isAccessible = !it.isHiddenForUsers,
                status = it.visibilityStatus.mapToDomain()
            )
        }
        return Page(
            count = data.counter,
            items = friends ?: emptyList(),
            offset = pageable.offset,
        )
    }
}
