package eu.peernetwork.persistence.domain.observable

import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservableBoolean @Inject constructor(
    private val repository: PreferenceRepository
) : Observable<Boolean> {
    override fun invoke(key: String): Flow<Boolean?> {
        return repository.observe(key, Boolean::class.java)
    }
}
