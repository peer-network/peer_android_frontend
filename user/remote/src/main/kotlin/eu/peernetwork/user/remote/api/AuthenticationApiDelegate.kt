package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.remote.extension.getError
import eu.peernetwork.core.remote.extension.getResponse
import eu.peernetwork.core.remote.extension.mapToDomain
import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.remote.mapper.mapToDomain
import public.eu.peernetwork.user.remote.LoginMutation
import javax.inject.Inject

class AuthenticationApiDelegate @Inject constructor(
    private val client: ApolloClient,
    private val listener: AuthenticationApi.Listener
) : AuthenticationApi {
    override suspend fun login(email: String, password: String): String {
        val query = LoginMutation(email, password)
        val response = client.mutation(query).execute()
        val data = response.getResponse().login
        val error = response.operation.getError(data.status.mapToDomain(), data.ResponseCode)
        if (error != null) {
            throw error
        }
        val token = data.mapToDomain()
        listener.onAuthenticationChanged(token)
        return token.access
    }
}
