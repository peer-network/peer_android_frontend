package eu.peernetwork.persistence.domain.retrievable

import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import javax.inject.Inject

class RetrievableInteger @Inject constructor(
    private val repository: PreferenceRepository
) : Retrievable<Int>{
    override fun invoke(key: String): Int? {
        return repository.get(key, Int::class.java)
    }
}
