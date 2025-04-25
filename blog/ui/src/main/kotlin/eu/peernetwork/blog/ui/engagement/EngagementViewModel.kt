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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    private val mutableState = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = mutableState.map {
        if (engagements.isNotEmpty()) {
            State.Content(
                isLoading = it is State.Loading,
                engagements = engagements,
                id = (it as? State.Success?)?.postId ?: (it as? State.Error?)?.postId,
                error = (it as? State.Error?)?.error
            )
        } else {
            it
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Default
    )

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
                mutableState.tryEmit(State.Loading)
                delay(50)
                block()
                engagements[previous.id] = contentUsecase(previous.id).mapToEngagement()
                mutableState.tryEmit(State.Success(previous.id))
            } catch (error: Throwable) {
                engagements[previous.id] = previous
                mutableState.tryEmit(State.Error(previous.id, error))
            } finally {
                mutexes.remove(previous.id)
            }
        }
    }

    fun clear() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Default)
        }
    }

    fun reset() {
        engagements.clear()
        viewModelScope.launch {
            mutableState.tryEmit(State.Default)
        }
    }

    sealed interface State {
        data object Default: State
        data object Loading: State
        data class Success(val postId: String): State
        data class Content(
            val id: String?,
            val isLoading: Boolean,
            val engagements: Map<String, UiEngagement>,
            val error: Throwable?
        ): State
        data class Error(val postId: String, val error: Throwable): State
    }
}
