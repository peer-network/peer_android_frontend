package eu.peernetwork.social.data.interactor

import eu.peernetwork.social.domain.interactor.ConnectionInteractor
import eu.peernetwork.social.domain.usecase.FollowUsecase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onSubscription
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionInteractorDelegate @Inject constructor(
    private val followUsecase: FollowUsecase
) : ConnectionInteractor {
    private val mutexes = ConcurrentHashMap.newKeySet<String>()

    private val connections = ConcurrentHashMap<String, Boolean>()

    private val state = MutableSharedFlow<Map<String, Boolean>>(replay = 1)

    override suspend fun connect(id: String, value: Boolean) {
        if (!mutexes.add(id)) return
        val previous = connections[id]
        try {
            connections[id] = value
            state.tryEmit(connections.toMap())
            connections[id] = followUsecase(id)
        } catch (error: Throwable) {
            if (previous == null) {
                connections.remove(id)
            } else {
                connections[id] = previous
            }
            throw error
        } finally {
            mutexes.remove(id)
            state.tryEmit(connections.toMap())
        }
    }

    override fun observe(): SharedFlow<Map<String, Boolean>> = state.onSubscription {
        state.tryEmit(connections.toMap())
    }

    override fun clear() {
        connections.clear()
        state.tryEmit(connections.toMap())
    }
}
