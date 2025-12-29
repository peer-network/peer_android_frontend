package eu.peernetwork.social.remote.api

import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.social.data.api.ModerationApi
import eu.peernetwork.social.domain.model.Block
import social.social.eu.peernetwork.social.remote.ListBlockedUsersQuery
import social.social.eu.peernetwork.social.remote.ToggleBlockUserStatusMutation
import javax.inject.Inject
import javax.inject.Named

class ModerationApiDelegate @Inject constructor(
    @Named("mediaUrl") private val url: String,
    private val client: RequestClient
): ModerationApi {
    override suspend fun get(userId: String, pageable: Pageable): Page<Block> {
        val query = ListBlockedUsersQuery(
            offset = Optional.present(pageable.offset),
            limit = Optional.present(pageable.limit)
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().listBlockedUsers
        response.assertOrThrow(data.status, data.ResponseCode)
        val blocked = data.affectedRows?.iBlocked?.map {
            Block(
                userId = userId,
                username = it.username!!,
                slug = it.slug!!,
                image = "$url/${it.img!!}".removeSuffix("/")
            )
        }

        return Page(
            count = data.counter,
            items = blocked ?: emptyList(),
            offset = pageable.offset
        )
    }

    override suspend fun block(userId: String): Boolean {
        val mutation = ToggleBlockUserStatusMutation(userId)
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().toggleBlockUserStatus
        response.assertOrThrow(data.status, data.ResponseCode)
        return true
    }

    override suspend fun report(userId: String): Boolean {
        TODO("Not yet implemented")
    }
}