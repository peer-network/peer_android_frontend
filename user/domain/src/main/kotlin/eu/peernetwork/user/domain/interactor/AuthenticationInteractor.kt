package eu.peernetwork.user.domain.interactor

import eu.peernetwork.user.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AuthenticationInteractor {
    fun observeAccount(): Flow<Account?>

    suspend fun getCurrentAccount(refresh: Boolean = false): Account
}
