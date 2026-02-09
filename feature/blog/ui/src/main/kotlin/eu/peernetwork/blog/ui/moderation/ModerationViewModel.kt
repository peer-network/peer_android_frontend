package eu.peernetwork.blog.ui.moderation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.blog.domain.usecase.ReportUsecase
import eu.peernetwork.blog.domain.usecase.SaveUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ModerationViewModel @Inject constructor(
    private val reportUsecase: ReportUsecase,
    private val saveUsecase: SaveUsecase
): ViewModel() {
    private val _state = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = _state.asStateFlow()

    fun report(postId: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            runCatching {
                reportUsecase(postId)
            }.onSuccess {
                _state.value = State.Success(postId)
            }.onFailure { error ->
                _state.value = State.Error(error)
            }
        }
    }

    fun save(postId: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            runCatching {
                saveUsecase(postId)
            }.onSuccess {
                _state.value = State.Success(postId)
            }.onFailure { error ->
                _state.value = State.Error(error)
            }
        }
    }

    fun reset() {
        _state.value = State.Default
    }

    sealed interface State {
        data object Default: State
        data object Loading: State
        data class Success(val postId: String): State
        data class Error(val error: Throwable): State
    }
}