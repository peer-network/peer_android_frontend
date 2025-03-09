package eu.peernetwork.persistence.domain.observable

import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservableString @Inject constructor(
    private val repository: PreferenceRepository
) : Observable<String> {
    override fun invoke(key: String): Flow<String?> {
        return repository[key, String::class.java]
    }
}
