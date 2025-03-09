package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.getResponse
import eu.peernetwork.user.data.api.SettingsApi
import protected.eu.peernetwork.user.remote.UpdateNameMutation
import javax.inject.Inject

class UsernameSettingsApi @Inject constructor(
    private val client: ApolloClient
) : SettingsApi.SecureUpdatable<String> {
    override suspend fun invoke(value: String, password: String) {
        val mutation = UpdateNameMutation(value, password)
        val response = client.mutation(mutation).execute()
        val data = response.getResponse().updateName
        response.assertOrThrow(data.status, data.ResponseCode)
    }
}
