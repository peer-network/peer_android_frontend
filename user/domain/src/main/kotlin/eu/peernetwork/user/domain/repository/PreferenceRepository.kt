package eu.peernetwork.user.domain.repository

import eu.peernetwork.user.domain.model.Preference

interface PreferenceRepository {
    suspend fun get(): Preference
}
