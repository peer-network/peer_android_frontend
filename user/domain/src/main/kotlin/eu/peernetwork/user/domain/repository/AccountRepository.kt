package eu.peernetwork.user.domain.repository

import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.model.Profile

interface AccountRepository {
    suspend fun get(id: String): Account

    suspend fun register(): String

    suspend fun update(profile: Profile): Account

    suspend fun changePassword(old: String, new: String)

    suspend fun delete(code: String)
}
