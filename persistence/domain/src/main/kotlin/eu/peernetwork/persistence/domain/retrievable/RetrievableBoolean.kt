package eu.peernetwork.persistence.domain.retrievable

import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import javax.inject.Inject

class RetrievableBoolean @Inject constructor(
    private val repository: PreferenceRepository
) : Retrievable<Boolean>{
    override fun invoke(key: String): Boolean? {
        return repository.get(key, Boolean::class.java)
    }
}
