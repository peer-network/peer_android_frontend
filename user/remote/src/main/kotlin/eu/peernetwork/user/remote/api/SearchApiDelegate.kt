package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.getResponse
import eu.peernetwork.user.data.api.SearchApi
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.remote.mapper.mapToDomain
import protected.eu.peernetwork.user.remote.SearchuserQuery
import javax.inject.Inject

class SearchApiDelegate @Inject constructor(
    private val client: ApolloClient
) : SearchApi {
    override suspend fun findByUsername(username: String, pageable: Pageable): List<Account> {
        val query = SearchuserQuery(username, pageable.offset, pageable.limit)
        val response = client.query(query).execute()
        val data = response.getResponse().searchuser
        response.assertOrThrow(data.status, data.ResponseCode)
        return data.affectedRows?.mapNotNull { it?.mapToDomain() } ?: emptyList()
    }
}
