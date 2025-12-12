package eu.peernetwork.blog.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.usecase.ViewUsecase
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.usecase.AuthorPostUsecase
import eu.peernetwork.core.common.paging.Pageable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArticleViewModel @Inject constructor(
    private val usecase: AuthorPostUsecase,
    private val viewUsecase: ViewUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<Map<Int, State>>(emptyMap())

    private val _status = MutableStateFlow<Map<String, Status>>(emptyMap())

    val states: StateFlow<Map<Int, State>> = _state.asStateFlow()

    val status: StateFlow<Map<String, Status>> = _status.asStateFlow()

    fun load(
        author: String,
        types: Set<Content.Type>,
        page: Pageable
    ) {
        val key = types.hashCode()
        viewModelScope.launch {
            usecase(
                AuthorPostUsecase.Parameter(
                    author = author,
                    types = types,
                    page = page
                )
            ).catch { updateState(key, State.Error(it)) }
                .onStart { updateState(key, State.Loading) }
                .cachedIn(viewModelScope)
                .apply {
                    collectLatest { updateState(key, State.Success(this)) }
                }
        }
    }

    private fun updateState(tab: Int, state: State) {
        _state.update { it + (tab to state) }
    }

    fun view(id: String) {
        viewModelScope.launch {
            try {
                updateStatus(id, Status.Loading)
                viewUsecase(id)
                updateStatus(id, Status.Success(id))
            } catch (error: Throwable) {
                updateStatus(id, Status.Error(error))
            }
        }
    }

    fun selected(tag: String, position: Int) {
        viewModelScope.launch {
            updateStatus(tag, Status.Success(position))
        }
    }

    fun updatedAt(page: Int, timestamp: Long) {
        viewModelScope.launch {
            updateStatus(page.toString(), Status.Success(timestamp))
        }
    }

    private fun updateStatus(tag: String, status: Status) {
        _status.update { it + (tag to status) }
    }

    sealed interface Status {
        data object Empty : Status
        data object Loading : Status
        data class Success<T>(val data: T) : Status
        data class Error(val error: Throwable) : Status
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val content: Flow<PagingData<UiPost>>) : State
        data class Error(val error: Throwable) : State
    }
}
