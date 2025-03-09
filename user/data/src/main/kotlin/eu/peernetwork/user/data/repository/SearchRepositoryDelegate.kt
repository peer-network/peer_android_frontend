package eu.peernetwork.user.data.repository

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.user.data.api.SearchApi
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.repository.SearchRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onSubscription
import javax.inject.Inject

class SearchRepositoryDelegate @Inject constructor(
    private val api: SearchApi
) : SearchRepository {
    private val users = MutableSharedFlow<List<Account>>(replay = REPLAY)

    override fun observe(): SharedFlow<List<Account>> = users.onSubscription {
        users.tryEmit(emptyList())
    }

    override suspend fun filterByUsername(username: String, pageable: Pageable) {
        users.tryEmit(api.findByUsername(username, pageable))
    }

    private companion object {
        const val REPLAY = 1
    }
}
