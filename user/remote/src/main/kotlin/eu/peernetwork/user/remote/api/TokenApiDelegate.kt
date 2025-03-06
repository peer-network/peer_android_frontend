package eu.peernetwork.user.remote.api

import eu.peernetwork.user.data.api.TokenApi
import eu.peernetwork.user.domain.model.Token

class TokenApiDelegate : TokenApi {
    override suspend fun refresh(token: String): Token {
        TODO("Not yet implemented")
    }
}
