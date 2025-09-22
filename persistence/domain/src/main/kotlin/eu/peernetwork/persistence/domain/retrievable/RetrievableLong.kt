package eu.peernetwork.persistence.domain.retrievable

import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import javax.inject.Inject

class RetrievableLong @Inject constructor(
    private val repository: PreferenceRepository
) : Retrievable<Long>{
    override fun invoke(key: String): Long? {
        return repository.get(key, Long::class.java)
    }
}
