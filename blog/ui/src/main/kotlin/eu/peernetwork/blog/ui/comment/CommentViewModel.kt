package eu.peernetwork.blog.ui.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.blog.domain.usecase.CommentUsecase
import eu.peernetwork.blog.ui.mapper.mapToComment
import eu.peernetwork.blog.ui.model.UiComment
import eu.peernetwork.blog.ui.usecase.CommentsUsecase
import eu.peernetwork.core.common.model.Pageable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommentViewModel @Inject constructor(
    private val usecase: CommentUsecase,
    private val commentsUsecase: CommentsUsecase
) : ViewModel() {
    private val content = MutableStateFlow<Flow<PagingData<UiComment>>?>(null)

    private val mutableState = MutableStateFlow<State>(State.Idle)

    val state: StateFlow<State> = combine(content, mutableState) { content, state ->
        if (content != null) {
            State.Content(
                isLoading = state is State.Loading,
                content = content,
                error = (state as? State.Error?)?.error
            )
        } else {
            state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Idle
    )

    fun load(postId: String, page: Pageable) {
        viewModelScope.launch {
            commentsUsecase(
                CommentsUsecase.Parameter(
                    id = postId,
                    page = page
                )
            ).catch { mutableState.tryEmit(State.Error(it)) }
                .onStart { mutableState.tryEmit(State.Loading) }
                .cachedIn(viewModelScope)
                .apply { collectLatest {
                    content.tryEmit(this)
                    mutableState.tryEmit(State.Idle)
                } }
        }
    }

    fun comment(postId: String, comment: String) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            try {
                usecase(CommentUsecase.Parameter(postId, comment)).mapToComment()
                mutableState.tryEmit(State.Idle)
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    fun reset() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Idle)
        }
    }

    sealed interface State {
        data object Idle : State
        data object Loading : State
        data class Content(
            val isLoading: Boolean,
            val content: Flow<PagingData<UiComment>>,
            val error: Throwable?,
        ) : State
        data class Error(val error: Throwable) : State
    }
}
