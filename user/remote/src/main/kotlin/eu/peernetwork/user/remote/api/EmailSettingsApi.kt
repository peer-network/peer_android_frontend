package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.user.data.api.SettingsApi
import protected.eu.peernetwork.user.remote.UpdateMailMutation
import javax.inject.Inject

class EmailSettingsApi @Inject constructor(
    private val client: ApolloClient
) : SettingsApi.SecureUpdatable<String> {
    override suspend fun invoke(value: String, password: String) {
        val mutation = UpdateMailMutation(value, password)
        val response = client.mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().updateEmail
        response.assertOrThrow(data.status, data.ResponseCode)
    }
}
