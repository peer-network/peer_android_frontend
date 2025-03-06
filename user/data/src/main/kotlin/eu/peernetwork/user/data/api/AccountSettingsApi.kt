package eu.peernetwork.user.data.api

interface AccountSettingsApi {
    suspend fun set(name: String, value: Any)
}
