package eu.peernetwork.user.data.api

import eu.peernetwork.user.domain.model.Token

interface TokenApi {
    suspend fun refresh(token: String): Token

    suspend fun verify(token: String)
}
