package eu.peernetwork.user.data.persistence

import eu.peernetwork.user.domain.model.Account

interface ProfilePersistence {
    suspend fun get(id: String): Account

    suspend fun save(account: Account)
}
