package eu.peernetwork.persistence.domain.observable

import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservableInteger @Inject constructor(
    private val repository: PreferenceRepository
) : Observable<Int> {
    override fun invoke(key: String): Flow<Int?> {
        return repository.observe(key, Int::class.java)
    }
}
