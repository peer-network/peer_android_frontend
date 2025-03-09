package eu.peernetwork.persistence.domain.publishable

import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import javax.inject.Inject

class PublishableLong @Inject constructor(
    private val repository: PreferenceRepository
) : Publishable<Long> {
    override suspend fun invoke(key: String, value: Long?) {
        return repository.set(key, value)
    }
}
