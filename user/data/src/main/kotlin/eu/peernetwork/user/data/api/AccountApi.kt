package eu.peernetwork.user.data.api

import eu.peernetwork.user.data.model.AccountModel
import eu.peernetwork.user.domain.model.UserDetail

interface AccountApi {
    suspend fun get(id: String, refresh: Boolean = false): AccountModel

    suspend fun register(detail: UserDetail, referral: String?): String

    suspend fun changePassword(old: String, new: String)

    suspend fun activate(code: String)

    suspend fun delete(password: String)
}
