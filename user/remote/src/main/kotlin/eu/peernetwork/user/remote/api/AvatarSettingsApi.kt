package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.remote.extension.getError
import eu.peernetwork.core.remote.extension.getResponse
import eu.peernetwork.core.remote.extension.mapToDomain
import eu.peernetwork.user.data.api.SettingsApi
import protected.eu.peernetwork.user.remote.UpdateProfilePictureMutation
import javax.inject.Inject
import javax.inject.Named

@Named("avatar")
class AvatarSettingsApi @Inject constructor(
    private val client: ApolloClient
) : SettingsApi.Updatable<String> {
    override suspend fun invoke(value: String) {
        val mutation = UpdateProfilePictureMutation(value)
        val response = client.mutation(mutation).execute()
        val data = response.getResponse().updateProfilePicture
        val error = response.operation.getError(data.status.mapToDomain(), data.ResponseCode)
        if (error != null) {
            throw error
        }
    }
}
