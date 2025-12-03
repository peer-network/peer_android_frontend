package eu.peernetwork.blog.ui.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.blog.ui.usecase.FeedUsecase
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.usecase.ViewUsecase
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

class TimelineViewModel @Inject constructor(
    private val usecase: FeedUsecase,
    private val viewUsecase: ViewUsecase
) : ViewModel() {
    private val _status = MutableStateFlow<Map<Int, Status>>(emptyMap())

    private val _state = MutableStateFlow<Map<Int, State>>(emptyMap())

    val status: StateFlow<Map<Int, Status>> = _status.asStateFlow()

    val state: StateFlow<Map<Int, State>> = _state.asStateFlow()

    fun load(
        page: Pageable,
        category: Category = Category.NONE,
        criteria: Criteria? = null
    ) {
        val key = listOf(category, criteria).hashCode()
        viewModelScope.launch {
            usecase(
                FeedUsecase.Parameter(
                    category = category,
                    criteria = criteria,
                    page = page
                )
            ).catch { updateState(key, State.Error(it)) }
                .onStart { updateState(key, State.Loading) }
                .cachedIn(viewModelScope)
                .apply {
                    collectLatest {
                        updateState(key, State.Success(
                            category = category,
                            criteria = criteria,
                            content = this
                        ))
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

    private fun updateState(key: Int, state: State) {
        _state.update { it + (key to state) }
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
        data class Success(
            val category: Category,
            val criteria: Criteria?,
            val content: Flow<PagingData<UiPost>>
        ) : State
        data class Error(val error: Throwable) : State
    }
}