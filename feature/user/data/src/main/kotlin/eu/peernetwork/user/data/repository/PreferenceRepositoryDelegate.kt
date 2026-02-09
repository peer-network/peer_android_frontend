package eu.peernetwork.user.data.repository

import eu.peernetwork.user.data.api.PreferenceApi
import eu.peernetwork.user.domain.model.Preference
import eu.peernetwork.user.domain.repository.PreferenceRepository
import javax.inject.Inject

class PreferenceRepositoryDelegate @Inject constructor(
    private val api: PreferenceApi
) : PreferenceRepository {
    override suspend fun get(): Preference {
        return api.get()
    }

    override suspend fun set(preference: Preference) {
        return api.set(preference)
    }
}
