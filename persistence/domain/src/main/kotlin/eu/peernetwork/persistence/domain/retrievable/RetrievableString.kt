package eu.peernetwork.persistence.domain.retrievable

import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import javax.inject.Inject

class RetrievableString @Inject constructor(
    private val repository: PreferenceRepository
) : Retrievable<String>{
    override fun invoke(key: String): String? {
        return repository.get(key, String::class.java)
    }
}
