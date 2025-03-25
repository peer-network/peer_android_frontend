package eu.peernetwork.user.domain.repository

import eu.peernetwork.user.domain.model.UserDetail
import eu.peernetwork.user.domain.model.Account

interface AccountRepository {
    suspend fun get(id: String): Account

    suspend fun register(detail: UserDetail): String

    suspend fun update(properties: Map<String, Any>)

    suspend fun update(properties: Map<String, Any>, password: String)

    suspend fun changePassword(old: String, new: String)

    suspend fun activate(code: String)

    suspend fun delete(password: String)
}
