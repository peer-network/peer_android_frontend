package eu.peernetwork.blog.data.interactor

import eu.peernetwork.blog.domain.interactor.EngagementInteractor
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.domain.repository.EngagementRepository
import eu.peernetwork.wallet.domain.repository.RewardRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onSubscription
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.collections.set

class EngagementInteractorDelegate @Inject constructor(
    private val repository: EngagementRepository,
    private val rewardRepository: RewardRepository
) : EngagementInteractor {
    private val mutexes = ConcurrentHashMap.newKeySet<String>()

    private val likes = ConcurrentHashMap<String, Boolean>()

    private val dislikes = ConcurrentHashMap<String, Boolean>()

    private val comments = ConcurrentHashMap<String, Int>()

    private val state = MutableSharedFlow<Map<String, EngagementInteractor.Reaction>>(replay = 1)

    override suspend fun like(id: String) {
        if (!mutexes.add(id)) return
        val previous = likes[id]
        try {
            likes[id] = true
            invalidate()
            repository.post(id, Engagement.Content.Like)
        } catch (error: Throwable) {
            if (previous == null) {
                likes.remove(id)
            } else {
                likes[id] = previous
            }
            throw error
        } finally {
            mutexes.remove(id)
            invalidate()
            refresh()
        }
    }

    override suspend fun dislike(id: String) {
        if (!mutexes.add(id)) return
        val previous = dislikes[id]
        try {
            dislikes[id] = true
            invalidate()
            repository.post(id, Engagement.Content.Dislike)
        } catch (error: Throwable) {
            if (previous == null) {
                dislikes.remove(id)
            } else {
                dislikes[id] = previous
            }
            throw error
        } finally {
            mutexes.remove(id)
            invalidate()
            refresh()
        }
    }

    override suspend fun comment(id: String) {
        comments[id] = comments.getOrDefault(id, 0) + 1
        invalidate()
    }

    private fun invalidate() {
        state.tryEmit(
            (likes.keys + dislikes.keys + comments.keys).associateWith { key ->
                EngagementInteractor.Reaction(likes[key], dislikes[key], comments[key])
            }
        )
    }

    private suspend fun refresh() {
        try {
            rewardRepository.get()
        } catch (error: Throwable) {
            error.printStackTrace()
        }
    }

    override fun observe(): SharedFlow<Map<String, EngagementInteractor.Reaction>> {
        return state.onSubscription { invalidate() }
    }

    override suspend fun clear() {
        likes.clear()
        dislikes.clear()
        comments.clear()
        invalidate()
        refresh()
    }
}
