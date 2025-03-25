package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.user.data.api.SearchApi
import eu.peernetwork.user.domain.model.User
import eu.peernetwork.user.remote.mapper.mapToDomain
import protected.eu.peernetwork.user.remote.SearchuserQuery
import javax.inject.Inject

class SearchApiDelegate @Inject constructor(
    private val client: ApolloClient
) : SearchApi {
    override suspend fun findByUsername(username: String, pageable: Pageable): List<User> {
        val query = SearchuserQuery(username, pageable.offset, pageable.limit)
        val response = client.query(query).executeOrThrow()
        val data = response.getOrThrow().searchuser
        response.assertOrThrow(data.status, data.ResponseCode)
        return data.affectedRows?.mapNotNull { it?.mapToDomain() } ?: emptyList()
    }
}
