package eu.peernetwork.user.data.api

import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.model.AccountDetail

interface AccountApi {
    suspend fun get(id: String): Account

    suspend fun register(detail: AccountDetail): String

    suspend fun changePassword(old: String, new: String)

    suspend fun activate(code: String)

    suspend fun delete(password: String)
}
