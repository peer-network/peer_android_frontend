package eu.peernetwork.persistence.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    operator fun<T> get(key: String, clazz: Class<T>): Flow<T?>

    suspend fun<T> set(key: String, value: T?)
}
