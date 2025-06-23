package eu.peernetwork.blog.ui.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.blog.domain.usecase.CommentLikeUsecase
import eu.peernetwork.blog.domain.usecase.CommentUpdateUsecase
import eu.peernetwork.blog.domain.usecase.CommentUsecase
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
    private val commentsUsecase: CommentsUsecase,
    private val updateUsecase: CommentUpdateUsecase,
    private val likeUsecase: CommentLikeUsecase
) : ViewModel() {
    private val content = MutableStateFlow<Flow<PagingData<UiComment>>?>(null)

    private val likes = mutableSetOf<UiComment>()

    private val mutableState = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = combine(
        content,
        mutableState
    ) { content, state ->
        if (content != null) {
            State.Content(
                isLoading = state is State.Loading,
                likes = likes,
                content = content,
                selected = (state as? State.Comment?)?.id,
                error = (state as? State.Error?)?.error
            )
        } else {
            state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Default
    )

    fun load(postId: String, page: Pageable) {
        viewModelScope.launch {
            likes.clear()
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
                    mutableState.tryEmit(State.Default)
                } }
        }
    }

    fun comment(postId: String, comment: String) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            try {
                mutableState.tryEmit(State.Comment(
                    usecase(CommentUsecase.Parameter(postId, comment)).id
                ))
                updateUsecase(postId)
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    fun like(comment: UiComment) {
        viewModelScope.launch {
            if (likes.contains(comment)) {
                return@launch
            }
            val update = comment.copy(likes = comment.likes + 1, isLiked = true)
            try {
                likes.add(update)
                mutableState.tryEmit(State.Comment(comment.id))
                likeUsecase(comment.id)
            } catch (error: Throwable) {
                likes.remove(update)
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    fun reset(force: Boolean = false) {
        viewModelScope.launch {
            if (force) {
                content.tryEmit(null)
            }
            mutableState.tryEmit(State.Default)
        }
    }

    sealed interface State {
        data object Default: State
        data object Loading: State
        data class Comment(val id: String): State
        data class Content(
            val isLoading: Boolean,
            val selected: String?,
            val likes: Set<UiComment>,
            val content: Flow<PagingData<UiComment>>,
            val error: Throwable?,
        ): State
        data class Error(val error: Throwable): State
    }
}
