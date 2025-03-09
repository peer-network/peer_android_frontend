package eu.peernetwork.persistence.domain.publishable

import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import javax.inject.Inject

class PublishableBoolean @Inject constructor(
    private val repository: PreferenceRepository
) : Publishable<Boolean> {
    override suspend fun invoke(key: String, value: Boolean?) {
        return repository.set(key, value)
    }
}
