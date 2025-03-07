package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.remote.extension.getError
import eu.peernetwork.core.remote.extension.getResponse
import eu.peernetwork.core.remote.extension.mapToDomain
import eu.peernetwork.user.data.api.SettingsApi
import protected.eu.peernetwork.user.remote.UpdateMailMutation
import javax.inject.Inject
import javax.inject.Named

@Named("email")
class EmailSettingsApi @Inject constructor(
    private val client: ApolloClient
) : SettingsApi.SecureUpdatable<String> {
    override suspend fun invoke(value: String, password: String) {
        val mutation = UpdateMailMutation(value, password)
        val response = client.mutation(mutation).execute()
        val data = response.getResponse().updateMail
        val error = response.operation.getError(data.status.mapToDomain(), data.ResponseCode)
        if (error != null) {
            throw error
        }
    }
}
