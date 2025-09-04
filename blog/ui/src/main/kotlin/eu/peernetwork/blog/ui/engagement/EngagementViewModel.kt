package eu.peernetwork.blog.ui.engagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.blog.domain.usecase.DislikeUsecase
import eu.peernetwork.blog.domain.usecase.LikeUsecase
import eu.peernetwork.blog.domain.usecase.ObserveReactionUsecase
import eu.peernetwork.blog.ui.mapper.mapFromDomain
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.blog.ui.model.UiReaction
import eu.peernetwork.core.common.interactor.NotificationInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class EngagementViewModel @Inject constructor(
    private val likeUsecase: LikeUsecase,
    private val dislikeUsecase: DislikeUsecase,
    private val observeReactionUsecase: ObserveReactionUsecase,
    private val interactor: NotificationInteractor
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Default)

    private val mutableReactions = MutableStateFlow<Map<String, UiReaction>>(emptyMap())

    val reactions: StateFlow<Map<String, UiReaction>> = mutableReactions.asStateFlow()

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            observeReactionUsecase().collectLatest { reactions ->
                mutableReactions.emit(reactions.mapValues { it.value.mapFromDomain() })
            }
        }
    }

    fun like(content: UiContent) {
        viewModelScope.launch {
            handleLike(content.id) {
                try {
                    interactor.send(
                        to = content.author.id,
                        action = "like",
                        message = content.title.text
                    )
                } catch (_: Throwable) {}
            }
        }
    }

    fun like(id: String) { viewModelScope.launch { handleLike(id) } }

    suspend fun handleLike(id: String, callback: suspend () -> Unit = {}) {
        mutableState.emit(State.Loading)
        runCatching {
            likeUsecase(id)
            mutableState.emit(State.Success(id))
            callback()
        }.onFailure {
            mutableState.emit(State.Error(id, it))
        }
    }

    fun dislike(id: String) {
        viewModelScope.launch {
            mutableState.emit(State.Loading)
            runCatching {
                dislikeUsecase(id)
                mutableState.emit(State.Success(id))
            }.onFailure {
                mutableState.emit(State.Error(id, it))
            }
        }
    }

    fun clear() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Default)
        }
    }

    fun reset() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Default)
        }
    }

    sealed interface State {
        data object Default: State
        data object Loading: State
        data class Success(val postId: String): State
        data class Error(val postId: String, val error: Throwable): State
    }
}
