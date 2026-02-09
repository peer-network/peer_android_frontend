package eu.peernetwork.user.data.repository

import com.google.gson.Gson
import eu.peernetwork.persistence.domain.observable.ObservableString
import eu.peernetwork.persistence.domain.publishable.PublishableString
import eu.peernetwork.persistence.domain.retrievable.RetrievableString
import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.data.api.TokenApi
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepositoryDelegate @Inject constructor(
    private val gson: Gson,
    private val api: TokenApi,
    private val publisher: PublishableString,
    private val observable: ObservableString,
    private val retrievableString: RetrievableString,
) : TokenRepository, AuthenticationApi.Listener {
    private var _token: Token? = null

    private val flow: MutableSharedFlow<Token?> = MutableSharedFlow(replay = 1)

    init { flow.tryEmit(_token) }

    override fun get(): Token? = _token ?: retrievableString(TAG)?.run {
        gson.fromJson(this, Token::class.java)
    }

    override fun observe(): Flow<Token?> {
        return flow.onStart {
            _token = observable(TAG)
                .firstOrNull()
                ?.let { gson.fromJson(it, Token::class.java) }
            flow.emit(_token)
        }
    }

    override suspend fun onAuthenticationChanged(token: Token?, remember: Boolean) {
        _token = token
        if (remember) {
            publisher(TAG, token?.let { gson.toJson(it) })
        }
        flow.tryEmit(_token)
    }

    override suspend fun refresh(token: String): Token {
        val token = api.refresh(token)
        onAuthenticationChanged(token)
        return token
    }

    override suspend fun verify(token: String) {
        return api.verify(token)
    }

    override suspend fun clear() {
        onAuthenticationChanged(null)
    }

    internal companion object {
        val TAG: String = TokenRepositoryDelegate::class.java.name
    }
}
