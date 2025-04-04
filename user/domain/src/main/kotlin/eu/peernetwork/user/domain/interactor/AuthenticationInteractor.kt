package eu.peernetwork.user.domain.interactor

import eu.peernetwork.user.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AuthenticationInteractor {
    suspend fun get(): String

    suspend fun getCurrentAccount(refresh: Boolean = false): Account

    fun observeAccount(): Flow<Account?>

    suspend fun logout()
}
