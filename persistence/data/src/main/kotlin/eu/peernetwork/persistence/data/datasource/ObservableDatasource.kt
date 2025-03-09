package eu.peernetwork.persistence.data.datasource

import kotlinx.coroutines.flow.Flow

interface ObservableDatasource {
    fun observeLong(key: String): Flow<Long?>

    fun observeBoolean(key: String): Flow<Boolean?>

    fun observeInteger(key: String): Flow<Int?>

    fun observeString(key: String): Flow<String?>
}
