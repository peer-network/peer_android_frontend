package eu.peernetwork.user.remote.api

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.user.data.api.SearchApi
import eu.peernetwork.user.domain.model.User
import eu.peernetwork.user.remote.mapper.mapToDomain
import protected.eu.peernetwork.user.remote.SearchuserQuery
import javax.inject.Inject
import javax.inject.Named

class SearchApiDelegate @Inject constructor(
    @Named("mediaUrl") private val url: String,
    private val client: RequestClient,
) : SearchApi {
    override suspend fun findByUsername(username: String, pageable: Pageable): Page<User> {
        val query = SearchuserQuery(username, pageable.offset, pageable.limit)
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().searchUser
        response.assertOrThrow(data.status, data.ResponseCode)
        val content = data.affectedRows?.mapNotNull { it?.mapToDomain()?.copy(imageUrl = "$url${it.img}") } ?: emptyList()
        return Page(
            count = data.counter,
            offset = pageable.offset,
            items = content
        )
    }
}
