package eu.peernetwork.persistence.data.datasource

interface RetrievableDatasource {
    fun getLong(key: String): Long?

    fun getBoolean(key: String): Boolean?

    fun getInteger(key: String): Int?

    fun getString(key: String): String?
}
