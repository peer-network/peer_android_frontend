package eu.peernetwork.user.remote.api

import eu.peernetwork.user.data.api.AccountSettingsApi

class UsernameSettingsApi : AccountSettingsApi {
    override suspend fun set(name: String, value: Any) {
        TODO("Not yet implemented")
    }
}
