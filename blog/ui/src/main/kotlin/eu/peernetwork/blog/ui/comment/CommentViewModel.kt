package eu.peernetwork.blog.ui.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.blog.domain.usecase.CommentLikeUsecase
import eu.peernetwork.blog.domain.usecase.CommentUpdateUsecase
import eu.peernetwork.blog.domain.usecase.CommentUsecase
import eu.peernetwork.blog.domain.usecase.ReportCommentUsecase
import eu.peernetwork.blog.ui.model.UiComment
import eu.peernetwork.blog.ui.usecase.CommentsUsecase
import eu.peernetwork.core.common.paging.Pageable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommentViewModel @Inject constructor(
    private val usecase: CommentUsecase,
    private val commentsUsecase: CommentsUsecase,
    private val updateUsecase: CommentUpdateUsecase,
    private val likeUsecase: CommentLikeUsecase,
    private val reportCommentUsecase: ReportCommentUsecase,
) : ViewModel() {
    private val cache = mutableMapOf<String, UiComment>()

    private val _likes = MutableStateFlow<Map<String, UiComment>>(emptyMap())

    private val _state = MutableStateFlow<State>(State.Empty)

    private val _status = MutableStateFlow<Status>(Status.Empty)

    val state: StateFlow<State> = _state.asStateFlow()

    val status: StateFlow<Status> = _status.asStateFlow()

    val likes: StateFlow<Map<String, UiComment>> = _likes.asStateFlow()

    fun load(postId: String, page: Pageable) {
        viewModelScope.launch {
            commentsUsecase(
                CommentsUsecase.Parameter(
                    id = postId,
                    page = page
                )
            ).catch { _state.tryEmit(State.Error(it)) }
                .onStart { _state.tryEmit(State.Loading) }
                .cachedIn(viewModelScope)
                .apply { collectLatest {
                    _state.tryEmit(State.Success(postId,this))
                } }
        }
    }

    fun comment(postId: String, comment: String) {
        viewModelScope.launch {
            _status.tryEmit(Status.Loading(Intent.Comment))
            try {
                _status.tryEmit(Status.Success(
                    Intent.Comment,
                    usecase(CommentUsecase.Parameter(postId, comment)).id
                ))
                updateUsecase(postId)
            } catch (error: Throwable) {
                _status.tryEmit(Status.Error(Intent.Comment, error))
            }
        }
    }

    fun like(comment: UiComment) {
        viewModelScope.launch {
            if (cache.contains(comment.id)) {
                return@launch
            }
            val update = comment.copy(likes = comment.likes + 1, isLiked = true)
            try {
                cache[update.id] = update
                _status.tryEmit(Status.Success(Intent.Like, comment.id))
                _likes.tryEmit(cache.toMap())
                likeUsecase(comment.id)
            } catch (error: Throwable) {
                cache.remove(update.id)
                _likes.tryEmit(cache.toMap())
                _status.tryEmit(Status.Error(Intent.Like, error))
            }
        }
    }

    fun report(id: String) {
        viewModelScope.launch {
            try {
                _status.tryEmit(Status.Loading(Intent.Report))
                reportCommentUsecase(id)
                _status.tryEmit(Status.Success(Intent.Report, id))
            } catch (error: Throwable) {
                _status.tryEmit(Status.Error(Intent.Report, error))
            }
        }
    }

    fun reset() {
        viewModelScope.launch {
            _state.tryEmit(State.Empty)
            _status.tryEmit(Status.Empty)
        }
    }

    sealed interface Intent {
        data object Idle: Intent
        data object Like: Intent
        data object Report: Intent
        data object Comment: Intent
    }

    sealed class Status(val intent: Intent) {
        data object Empty: Status(Intent.Idle)
        data class Loading(val action: Intent): Status(action)
        data class Success<T>(val action: Intent, val content: T): Status(action)
        data class Error(val action: Intent, val error: Throwable): Status(action)
    }

    sealed interface State {
        data object Empty: State
        data object Loading: State
        data class Success(
            val postId: String,
            val content: Flow<PagingData<UiComment>>
        ): State
        data class Error(val error: Throwable): State
    }
}
