package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.remote.mapper.mapToDomain
import public.eu.peernetwork.user.remote.LoginMutation
import public.eu.peernetwork.user.remote.RefreshTokenMutation
import javax.inject.Inject

class AuthenticationApiDelegate @Inject constructor(
    private val client: ApolloClient,
    private val listener: AuthenticationApi.Listener
) : AuthenticationApi {
    override suspend fun login(email: String, password: String): String {
        val query = LoginMutation(email, password)
        val response = client.mutation(query).executeOrThrow()
        val data = response.getOrThrow().login
        response.assertOrThrow(data.status, data.ResponseCode)
        val token = data.mapToDomain()
        listener.onAuthenticationChanged(token)
        return token.access
    }

    override suspend fun refresh(token: String) {
        val mutation = RefreshTokenMutation(token)
        val response = client.mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().refreshToken
        response.assertOrThrow(data.status, data.ResponseCode)
        listener.onAuthenticationChanged(data.mapToDomain())
    }

    override suspend fun logout() {
        listener.onAuthenticationChanged(null)
    }
}
