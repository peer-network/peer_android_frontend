package eu.peernetwork.persistence.domain.observable

import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservableLong @Inject constructor(
    private val repository: PreferenceRepository
) : Observable<Long> {
    override fun invoke(key: String): Flow<Long?> {
        return repository.observe(key, Long::class.java)
    }
}
