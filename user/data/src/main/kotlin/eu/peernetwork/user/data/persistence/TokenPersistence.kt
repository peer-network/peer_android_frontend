package eu.peernetwork.user.data.persistence

import eu.peernetwork.user.domain.model.Token

interface TokenPersistence {
    suspend fun get(): Token?

    suspend fun save(token: Token)

    suspend fun clear()
}
