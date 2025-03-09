package eu.peernetwork.persistence.data.repository

import eu.peernetwork.persistence.data.datasource.PublishableDatasource
import eu.peernetwork.persistence.data.datasource.ObservableDatasource
import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import eu.peernetwork.persistence.domain.exception.UnsupportedTypeException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceRepositoryDelegate @Inject constructor(
    private val observable: ObservableDatasource,
    private val publishable: PublishableDatasource,
) : PreferenceRepository {
    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: String, clazz: Class<T>): Flow<T?> {
        return when (clazz) {
            String::class.java -> observable.observeString(key)
            Int::class.java -> observable.observeInteger(key)
            Boolean::class.java -> observable.observeBoolean(key)
            Long::class.java -> observable.observeLong(key)
            else -> throw UnsupportedTypeException(clazz.name)
        }.map { it as T? }
    }

    override suspend fun <T> set(key: String, value: T?) {
        when (value) {
            is String -> publishable.putString(key, value)
            is Int -> publishable.putInteger(key, value)
            is Boolean -> publishable.putBoolean(key, value)
            is Long -> publishable.putLong(key, value)
            null -> publishable.remove(key)
            else -> value.run {
                throw UnsupportedTypeException(this::class.java.name)
            }
        }
    }
}
