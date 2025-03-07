package eu.peernetwork.user.domain.repository

import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.model.AccountDetail

interface AccountRepository {
    suspend fun get(id: String): Account

    suspend fun register(detail: AccountDetail): String

    suspend fun update(properties: Map<String, Any>): Account

    suspend fun changePassword(old: String, new: String)

    suspend fun delete(password: String)
}
