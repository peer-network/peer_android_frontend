package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.remote.extension.getResponse
import eu.peernetwork.user.data.api.AccountApi
import eu.peernetwork.user.data.exception.UserNotFoundException
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.remote.mapper.mapToDomain
import protected.eu.peernetwork.user.remote.ProfileQuery

class AccountApiDelegate(
    private val client: ApolloClient
) : AccountApi {
    override suspend fun get(id: String): Account {
        return client.query(ProfileQuery(Optional.present(id))).execute()
            .getResponse()
            .profile
            .affectedRows
            ?.mapToDomain() ?: throw UserNotFoundException()
    }

    override suspend fun register(): String {
        TODO("Not yet implemented")
    }

    override suspend fun changePassword(old: String, new: String) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(code: String) {
        TODO("Not yet implemented")
    }
}
