package eu.peernetwork.blog.ui.engagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.blog.domain.usecase.ContentUsecase
import eu.peernetwork.blog.domain.usecase.DislikeUsecase
import eu.peernetwork.blog.domain.usecase.LikeUsecase
import eu.peernetwork.blog.ui.mapper.mapToEngagement
import eu.peernetwork.blog.ui.model.UiEngagement
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.collections.set

class EngagementViewModel @Inject constructor(
    private val likeUsecase: LikeUsecase,
    private val dislikeUsecase: DislikeUsecase,
    private val contentUsecase: ContentUsecase
) : ViewModel() {
    private val mutexes = ConcurrentHashMap.newKeySet<String>()

    private val engagements = mutableMapOf<String, UiEngagement>()

    private val mutableState = MutableStateFlow<State>(State.Idle(engagements))

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun like(engagement: UiEngagement) {
        if (engagements[engagement.id]?.isLiked == true || engagement.isLiked) {
            return
        }
        val next = engagements[engagement.id] ?: engagement
        handleEngagement(engagement, next.copy(
            likes = engagement.likes + 1,
            isLiked = true,
        )) { likeUsecase(engagement.id) }
    }

    fun dislike(engagement: UiEngagement) {
        if (engagements[engagement.id]?.isDisliked == true || engagement.isDisliked) {
            return
        }
        val next = engagements[engagement.id] ?: engagement
        handleEngagement(engagement, next.copy(
            dislikes = engagement.dislikes + 1,
            isDisliked = true,
        )) { dislikeUsecase(engagement.id) }
    }

    fun comment(engagement: UiEngagement) {
        val next = engagements[engagement.id] ?: engagement
        handleEngagement(engagement, next.copy(comment = engagement.comment + 1)) {  }
    }

    private fun handleEngagement(
        previous: UiEngagement,
        next: UiEngagement,
        block: suspend () -> Unit
    ) {
        if (mutexes.contains(previous.id)) { return }
        mutexes.add(previous.id)
        engagements[previous.id] = next
        viewModelScope.launch {
            try {
                mutableState.tryEmit(State.Loading(engagements))
                delay(50)
                block()
                engagements[previous.id] = contentUsecase(previous.id).mapToEngagement()
                mutableState.tryEmit(State.Success(engagements))
            } catch (error: Throwable) {
                engagements[previous.id] = previous
                mutableState.tryEmit(State.Error(previous.id, engagements, error))
            } finally {
                mutexes.remove(previous.id)
            }
        }
    }

    fun clean() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Idle(engagements))
        }
    }

    fun reset() {
        engagements.clear()
        viewModelScope.launch {
            mutableState.tryEmit(State.Idle(engagements))
        }
    }

    sealed class State(val engagements: Map<String, UiEngagement>) {
        data class Idle(val model: Map<String, UiEngagement>): State(model)
        data class Loading(val model: Map<String, UiEngagement>): State(model)
        data class Success(val model: Map<String, UiEngagement>): State(model)
        data class Error(
            val selected: String,
            val model: Map<String, UiEngagement>,
            val error: Throwable
        ): State(model)
    }
}
