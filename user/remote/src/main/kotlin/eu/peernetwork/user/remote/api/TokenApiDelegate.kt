package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.remote.extension.getError
import eu.peernetwork.core.remote.extension.getResponse
import eu.peernetwork.core.remote.extension.mapToDomain
import eu.peernetwork.user.data.api.TokenApi
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.remote.mapper.mapToDomain
import public.eu.peernetwork.user.remote.RefreshTokenMutation
import javax.inject.Inject

class TokenApiDelegate @Inject constructor(
    private val client: ApolloClient
) : TokenApi {
    override suspend fun refresh(token: String): Token {
        val mutation = RefreshTokenMutation(token)
        val response = client.mutation(mutation).execute()
        val data = response.getResponse().refreshToken
        val error = response.operation.getError(data.status.mapToDomain(), data.ResponseCode)
        if (error != null) {
            throw error
        }
        return data.mapToDomain()
    }
}
