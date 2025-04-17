package eu.peernetwork.blog.ui.engagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.blog.domain.usecase.ContentUsecase
import eu.peernetwork.blog.domain.usecase.DislikeUsecase
import eu.peernetwork.blog.domain.usecase.LikeUsecase
import eu.peernetwork.blog.ui.mapper.mapToEngagement
import eu.peernetwork.blog.ui.model.UiEngagement
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

    private val mutableState = MutableStateFlow<State>(State.Empty(engagements))

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun like(engagement: UiEngagement) {
        handleEngagement(engagement) {
            update(engagement.copy(
                likes = engagement.likes + 1,
                isLiked = true,
            ))
            likeUsecase(engagement.id)
        }
    }

    fun dislike(engagement: UiEngagement) {
        handleEngagement(engagement) {
            update(engagement.copy(
                dislikes = engagement.dislikes + 1,
                isDisliked = true,
            ))
            dislikeUsecase(engagement.id)
        }
    }

    private fun handleEngagement(engagement: UiEngagement, block: suspend () -> Unit) {
        viewModelScope.launch {
            if (mutexes.contains(engagement.id)) {
                return@launch
            }
            mutexes.add(engagement.id)
            try {
                block()
                engagements[engagement.id] = contentUsecase(engagement.id).mapToEngagement()
                mutableState.tryEmit(State.Success(engagements))
            } catch (error: Throwable) {
                engagements[engagement.id] = engagement
                mutableState.tryEmit(State.Error(engagement.id, engagements, error))
            } finally {
                mutexes.remove(engagement.id)
            }
        }
    }

    private fun update(engagement: UiEngagement) {
        engagements[engagement.id] = engagement
        mutableState.tryEmit(State.Success(engagements))
    }

    fun reset() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Empty(engagements))
        }
    }

    sealed class State(val engagements: Map<String, UiEngagement>) {
        data class Empty(val model: Map<String, UiEngagement>): State(model)
        data class Loading(val model: Map<String, UiEngagement>): State(model)
        data class Success(val model: Map<String, UiEngagement>): State(model)
        data class Error(
            val selected: String,
            val model: Map<String, UiEngagement>,
            val error: Throwable
        ): State(model)
    }
}
