package eu.peernetwork.blog.data.interactor

import eu.peernetwork.blog.domain.interactor.EngagementInteractor
import eu.peernetwork.blog.domain.usecase.DislikeUsecase
import eu.peernetwork.blog.domain.usecase.LikeUsecase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onSubscription
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.collections.set

class EngagementInteractorDelegate @Inject constructor(
    private val likeUsecase: LikeUsecase,
    private val dislikeUsecase: DislikeUsecase,
) : EngagementInteractor {
    private val mutexes = ConcurrentHashMap.newKeySet<String>()

    private val reactions = ConcurrentHashMap<String, EngagementInteractor.Reaction>()

    private val state = MutableSharedFlow<Map<String, EngagementInteractor.Reaction>>(replay = 1)

    override suspend fun like(id: String) {
        if (!mutexes.add(id)) return
        val previous = reactions[id]
        try {
            reactions[id] = previous?.copy(like = true)
                ?: EngagementInteractor.Reaction(
                    like = true,
                    dislike = false
                )
            likeUsecase(id)
            state.tryEmit(reactions)
        } catch (error: Throwable) {
            if (previous == null) {
                reactions.remove(id)
            } else {
                reactions[id] = previous
            }
            throw error
        } finally {
            mutexes.remove(id)
        }
    }

    override suspend fun dislike(id: String) {
        if (!mutexes.add(id)) return
        val previous = reactions[id]
        try {
            reactions[id] = previous?.copy(like = true)
                ?: EngagementInteractor.Reaction(
                    like = false,
                    dislike = true
                )
            dislikeUsecase(id)
            state.tryEmit(reactions)
        } catch (error: Throwable) {
            if (previous == null) {
                reactions.remove(id)
            } else {
                reactions[id] = previous
            }
            throw error
        } finally {
            mutexes.remove(id)
        }
    }

    override fun observe(): SharedFlow<Map<String, EngagementInteractor.Reaction>> {
        return state.onSubscription { emit(reactions) }
    }
}
