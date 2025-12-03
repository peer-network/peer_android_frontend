package eu.peernetwork.blog.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Sort
import eu.peernetwork.blog.domain.usecase.ViewUsecase
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.blog.ui.usecase.ExplorePostsUsecase
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
import kotlin.collections.plus

class ExploreViewModel @Inject constructor(
    private val usecase: ExplorePostsUsecase,
    private val viewUsecase: ViewUsecase,
): ViewModel() {
    private val _status = MutableStateFlow<Map<Int, Status>>(emptyMap())

    private val _state = MutableStateFlow<State>(State.Empty)

    val status: StateFlow<Map<Int, Status>> = _status.asStateFlow()

    val state: StateFlow<State> = _state.asStateFlow()

    fun get(page: Pageable) {
        viewModelScope.launch {
            usecase(
                ExplorePostsUsecase.Parameter(
                    criteria = Criteria.Content(sort = Sort.TREND),
                    page = page
                )
            )
                .catch { _state.tryEmit(State.Error(it)) }
                .onStart { _state.tryEmit(State.Loading) }
                .cachedIn(viewModelScope)
                .apply {
                    collectLatest {
                        _state.tryEmit(State.Success(this))
                    }
                }
        }
    }

    fun view(id: String) {
        viewModelScope.launch {
            try {
                viewUsecase(id)
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

    fun selected(tag: Int, position: Int) {
        viewModelScope.launch {
            updateStatus(tag, Status.Success(position))
        }
    }

    private fun updateStatus(tag: Int, status: Status) {
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
