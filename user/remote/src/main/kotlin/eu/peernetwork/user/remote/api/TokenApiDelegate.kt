package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.user.data.api.TokenApi
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.remote.mapper.mapToDomain
import eu.peernetwork.user.remote.usecase.JwtLifecycleUsecase
import public.eu.peernetwork.user.remote.RefreshTokenMutation
import javax.inject.Inject

class TokenApiDelegate @Inject constructor(
    private val client: ApolloClient,
    private val usecase: JwtLifecycleUsecase
) : TokenApi {
    override suspend fun refresh(token: String): Token {
        val mutation = RefreshTokenMutation(token)
        val response = client.mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().refreshToken
        response.assertOrThrow(data.status, data.ResponseCode)
        val token = data.mapToDomain()
        return token.copy(expiresIn = usecase(token.access))
    }
}
