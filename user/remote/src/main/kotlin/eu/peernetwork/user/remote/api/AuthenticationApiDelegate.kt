package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.domain.exception.AccountNotFoundException
import eu.peernetwork.user.remote.mapper.mapToDomain
import eu.peernetwork.user.remote.usecase.JwtExpiryUsecase
import public.eu.peernetwork.user.remote.HelloQuery
import public.eu.peernetwork.user.remote.LoginMutation
import javax.inject.Inject

class AuthenticationApiDelegate @Inject constructor(
    private val client: ApolloClient,
    private val usecase: JwtExpiryUsecase,
    private val listener: AuthenticationApi.Listener
) : AuthenticationApi {
    override suspend fun authenticated(): String {
        val query = HelloQuery()
        val response = client.query(query).executeOrThrow()
        val userId = response.getOrThrow().hello?.currentuserid
        if (userId.isNullOrEmpty()) {
            throw AccountNotFoundException()
        }
        return userId
    }

    override suspend fun login(email: String, password: String): String {
        val query = LoginMutation(email, password)
        val response = client.mutation(query).executeOrThrow()
        val data = response.getOrThrow().login
        response.assertOrThrow(data.status, data.ResponseCode)
        val token = data.mapToDomain()
        listener.onAuthenticationChanged(token.copy(
            expiresIn = usecase(token.access)
        ))
        return token.access
    }

    override suspend fun logout() {
        listener.onAuthenticationChanged(null)
    }
}
