package eu.peernetwork.user.data.api

import eu.peernetwork.user.domain.model.Preference

interface PreferenceApi {
    suspend fun get(): Preference

    suspend fun set(preference: Preference)
}
