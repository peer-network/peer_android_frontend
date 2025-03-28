package eu.peernetwork.app.api

import android.util.Base64
import eu.peernetwork.user.data.api.SettingsApi
import eu.peernetwork.user.remote.api.BiographySettingsApi
import javax.inject.Inject

class BiographyApi @Inject constructor(
    private val api: BiographySettingsApi
) : SettingsApi.Updatable<String> {
    override suspend fun invoke(value: String) {
        val bio = Base64.encodeToString(
            value.toByteArray(Charsets.UTF_8),
            Base64.DEFAULT
        )
        api("data:text/plain;base64,$bio")
    }
}
