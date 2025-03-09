package eu.peernetwork.persistence.data.datasource

interface PublishableDatasource {
    suspend fun putLong(key: String, value: Long)

    suspend fun putBoolean(key: String, value: Boolean)

    suspend fun putInteger(key: String, value: Int)

    suspend fun putString(key: String, value: String)

    suspend fun remove(key: String)
}
