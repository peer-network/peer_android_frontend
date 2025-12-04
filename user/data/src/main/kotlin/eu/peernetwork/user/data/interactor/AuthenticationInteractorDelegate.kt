package eu.peernetwork.user.data.interactor

import com.google.gson.Gson
import eu.peernetwork.persistence.domain.observable.ObservableString
import eu.peernetwork.persistence.domain.publishable.PublishableString
import eu.peernetwork.persistence.domain.retrievable.RetrievableString
import eu.peernetwork.user.domain.exception.AccountNotFoundException
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
    private val retrievable: RetrievableString,
    private val repository: AccountRepository,
    private val authenticationRepository: AuthenticationRepository
) : AuthenticationInteractor {
    override suspend fun get(): String {
        val currentUser = retrievable(USER_KEY)
        if (currentUser != null) {
            return currentUser
        }
        val user = authenticationRepository.authenticated()
        publisher(USER_KEY, user)
        return user
    }

    override suspend fun getCurrentAccount(refresh: Boolean): Account {
        return try {
            val json = retrievable(ACCOUNT_KEY)
            if (refresh || json == null) {
                val user = authenticationRepository.authenticated()
                repository.get(user, true).also {
                    publisher(ACCOUNT_KEY, gson.toJson(it))
                }
            } else {
                gson.fromJson(json, Account::class.java)
            }
        } catch (error: Throwable) {
            if (error is AccountNotFoundException) {
                logout()
            }
            throw error
        }
    }

    override fun observeAccount(): Flow<Account?> = observable(ACCOUNT_KEY).map {
        gson.fromJson(it, Account::class.java)
    }

    override suspend fun logout() {
        publisher(USER_KEY, null)
        publisher(ACCOUNT_KEY, null)
        authenticationRepository.logout()
    }

    internal companion object {
        const val USER_KEY: String = "eu.peernetwork.user.data.interactor.USER_KEY"
        const val ACCOUNT_KEY: String = "eu.peernetwork.user.data.interactor.ACCOUNT_KEY"
    }
}
