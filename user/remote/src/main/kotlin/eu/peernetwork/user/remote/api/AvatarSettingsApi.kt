package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.core.remote.provider.NetworkProvider
import eu.peernetwork.user.data.api.SettingsApi
import protected.eu.peernetwork.user.remote.UpdateProfilePictureMutation
import javax.inject.Inject

class AvatarSettingsApi @Inject constructor(
    private val provider: NetworkProvider,
) : SettingsApi.Updatable<String> {
    override suspend fun invoke(value: String) {
        val mutation = UpdateProfilePictureMutation(value)
        val response = provider.client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().updateProfileImage
        response.assertOrThrow(data.status, data.ResponseCode)
    }
}
