package eu.peernetwork.persistence.domain.observable

import kotlinx.coroutines.flow.Flow

interface Observable<T> {
    operator fun invoke(key: String): Flow<T?>
}
