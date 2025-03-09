package eu.peernetwork.persistence.domain.publishable

import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import javax.inject.Inject

class PublishableString @Inject constructor(
    private val repository: PreferenceRepository
) : Publishable<String> {
    override suspend fun invoke(key: String, value: String?) {
        return repository.set(key, value)
    }
}
