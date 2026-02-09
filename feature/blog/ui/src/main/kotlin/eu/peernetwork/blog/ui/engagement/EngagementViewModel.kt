package eu.peernetwork.blog.ui.engagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.blog.domain.usecase.DislikeUsecase
import eu.peernetwork.blog.domain.usecase.LikeUsecase
import eu.peernetwork.blog.domain.usecase.ObserveReactionUsecase
import eu.peernetwork.blog.ui.mapper.mapFromDomain
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
    private val _state = MutableStateFlow<State>(State.Default)

    private val _reactions = MutableStateFlow<Map<String, UiReaction>>(emptyMap())

    val reactions: StateFlow<Map<String, UiReaction>> = _reactions.asStateFlow()

    val state: StateFlow<State> = _state.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            observeReactionUsecase().collectLatest { reactions ->
                 _reactions.emit(reactions.mapValues { it.value.mapFromDomain() })
            }
        }
    }

    fun like(id: String, author: String, message: String) {
        viewModelScope.launch {
            handleLike(id) {
                try {
                    interactor.send(
                        to = author,
                        action = "like",
                        message = message
                    )
                } catch (_: Throwable) {}
            }
        }
    }

    fun like(id: String) { viewModelScope.launch { handleLike(id) } }

    suspend fun handleLike(id: String, callback: suspend () -> Unit = {}) {
        _state.emit(State.Loading)
        runCatching {
            likeUsecase(id)
            _state.emit(State.Success(id))
            callback()
        }.onFailure {
            _state.emit(State.Error(id, it))
        }
    }

    fun dislike(id: String) {
        viewModelScope.launch {
            _state.emit(State.Loading)
            runCatching {
                dislikeUsecase(id)
                _state.emit(State.Success(id))
            }.onFailure {
                _state.emit(State.Error(id, it))
            }
        }
    }

    fun clear() {
        viewModelScope.launch {
            _state.tryEmit(State.Default)
        }
    }

    fun reset() {
        viewModelScope.launch {
            _state.tryEmit(State.Default)
        }
    }

    sealed interface State {
        data object Default: State
        data object Loading: State
        data class Success(val postId: String): State
        data class Error(val postId: String, val error: Throwable): State
    }
}
