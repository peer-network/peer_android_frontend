package eu.peernetwork.user.data.repository

import com.google.gson.Gson
import eu.peernetwork.persistence.domain.observable.ObservableString
import eu.peernetwork.persistence.domain.publishable.PublishableString
import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class TokenRepositoryDelegate @Inject constructor(
    private val gson: Gson,
    private val publisher: PublishableString,
    private val observable: ObservableString,
) : TokenRepository, AuthenticationApi.Listener {
    private var lastToken: Token? = null

    override fun get(): Token? = lastToken

    override fun observe(): Flow<Token?> {
        return observable(TAG).map { token ->
            token?.let { gson.fromJson(it, Token::class.java) }
        }.onEach { lastToken = it }
    }

    override suspend fun onAuthenticationChanged(token: Token?) {
        if (token != lastToken) {
            lastToken = token
            publisher(TAG, token?.let { gson.toJson(it) })
        }
    }

    private companion object {
        val TAG: String = TokenRepositoryDelegate::class.java.name
    }
}
