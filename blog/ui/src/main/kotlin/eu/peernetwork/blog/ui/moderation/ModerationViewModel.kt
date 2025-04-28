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
    private val mutableState = MutableStateFlow<State>(State.Default)
    val state: StateFlow<State> = mutableState.asStateFlow()

    fun report(postId: String) {
        viewModelScope.launch {
            mutableState.value = State.Loading
            runCatching {
                reportUsecase(postId)
            }.onSuccess {
                mutableState.value = State.Success(postId)
            }.onFailure { error ->
                mutableState.value = State.Error(error, postId)
            }
        }
    }

    fun save(postId: String) {
        viewModelScope.launch {
            mutableState.value = State.Loading
            runCatching {
                saveUsecase(postId)
            }.onSuccess {
                mutableState.value = State.Success(postId)
            }.onFailure { error ->
                mutableState.value = State.Error(error, postId)
            }
        }
    }

    fun reset() {
        mutableState.value = State.Default
    }

    sealed interface State {
        data object Default: State
        data object Loading: State
        data class Success(val postId: String): State
        data class Error(val error: Throwable, val postId: String): State
    }
}