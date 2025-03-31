package eu.peernetwork.user.data.interactor

import com.google.gson.Gson
import eu.peernetwork.persistence.domain.observable.ObservableString
import eu.peernetwork.persistence.domain.publishable.PublishableString
import eu.peernetwork.user.domain.interactor.AuthenticationInteractor
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.repository.AccountRepository
import eu.peernetwork.user.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthenticationInteractorDelegate @Inject constructor(
    private val gson: Gson,
    private val publisher: PublishableString,
    private val observable: ObservableString,
    private val repository: AccountRepository,
    private val authenticationRepository: AuthenticationRepository,
) : AuthenticationInteractor {
    private val tag = this::class.java.name

    override fun observeAccount(): Flow<Account?> = observable(tag).map {
        gson.fromJson(it, Account::class.java)
    }

    override suspend fun getCurrentAccount(refresh: Boolean): Account {
        return repository.get(authenticationRepository.authenticated(), refresh).also {
            publisher(tag, gson.toJson(it))
        }
    }

    override suspend fun logout() {
        publisher(tag, null)
        authenticationRepository.logout()
    }
}
