package eu.peernetwork.user.domain.repository

import eu.peernetwork.user.domain.model.Token
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    fun get(): Token?

    fun observe(): Flow<Token?>
}
