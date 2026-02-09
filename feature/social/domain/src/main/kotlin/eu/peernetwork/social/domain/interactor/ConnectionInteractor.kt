package eu.peernetwork.social.domain.interactor

import kotlinx.coroutines.flow.SharedFlow

interface ConnectionInteractor {
    suspend fun connect(id: String, value: Boolean)

    fun observe(): SharedFlow<Map<String, Boolean>>

    fun clear()
}
