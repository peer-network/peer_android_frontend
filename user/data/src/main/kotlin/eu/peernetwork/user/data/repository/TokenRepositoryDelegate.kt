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
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenRepositoryDelegate @Inject constructor(
    private val gson: Gson,
    private val api: TokenApi,
    private val publisher: PublishableString,
    private val observable: ObservableString,
    private val retrievableString: RetrievableString,
) : TokenRepository, AuthenticationApi.Listener {

    override fun get(): Token? = retrievableString(TAG)?.run {
        gson.fromJson(this, Token::class.java)
    }

    override fun observe(): Flow<Token?> {
        return observable(TAG).map { token ->
            token?.let { gson.fromJson(it, Token::class.java) }
        }
    }

    override suspend fun onAuthenticationChanged(token: Token?) {
        publisher(TAG, token?.let { gson.toJson(it) })
    }

    override suspend fun refresh(token: String): Token {
        val token = api.refresh(token)
        onAuthenticationChanged(token)
        return token
    }

    override suspend fun clear() {
        onAuthenticationChanged(null)
    }

    internal companion object {
        val TAG: String = TokenRepositoryDelegate::class.java.name
    }
}
