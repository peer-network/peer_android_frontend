package eu.peernetwork.user.data.api

import eu.peernetwork.user.domain.model.Account

interface AccountApi {
    suspend fun get(id: String): Account

    suspend fun register(): String

    suspend fun changePassword(old: String, new: String)

    suspend fun delete(code: String)
}
