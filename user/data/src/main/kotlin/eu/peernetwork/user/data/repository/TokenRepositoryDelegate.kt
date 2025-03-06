package eu.peernetwork.user.data.repository

import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.data.api.TokenApi
import eu.peernetwork.user.data.persistence.TokenPersistence
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onSubscription

class TokenRepositoryDelegate(
    private val api: TokenApi,
    private val persistence: TokenPersistence
) : TokenRepository, AuthenticationApi.Listener {
    private var lastToken: Token? = null

    private val observer = MutableSharedFlow<Token?>(replay = REPLAY)

    override fun get(): Token? = lastToken

    override fun observe(): Flow<Token?> {
        return observer.onSubscription {
            observer.tryEmit(persistence.get())
        }.onEach { lastToken = it }
    }

    override suspend fun onAuthenticationChanged(token: Token?) {
        token?.let { persistence.save(it) } ?: persistence.clear()
        lastToken = token
        observer.tryEmit(token)
    }

    override suspend fun refresh(token: String): Token {
        val refreshedToken = api.refresh(token)
        onAuthenticationChanged(refreshedToken)
        return refreshedToken
    }

    private companion object {
        const val REPLAY = 1
    }
}
