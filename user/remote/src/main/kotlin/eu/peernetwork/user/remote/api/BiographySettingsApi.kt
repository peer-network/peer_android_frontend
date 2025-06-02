package eu.peernetwork.user.remote.api

import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.user.data.api.SettingsApi
import protected.eu.peernetwork.user.remote.UpdateBiographyMutation
import javax.inject.Inject

class BiographySettingsApi @Inject constructor(
    private val client: RequestClient,
) : SettingsApi.Attribute<String> {
    override suspend fun invoke(value: String) {
        val mutation = UpdateBiographyMutation(value)
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().updateBio
        response.assertOrThrow(data.status, data.ResponseCode)
    }
}
