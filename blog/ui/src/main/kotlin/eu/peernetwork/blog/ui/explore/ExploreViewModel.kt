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
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExploreViewModel @Inject constructor(
    private val usecase: ExplorePostsUsecase,
    private val viewUsecase: ViewUsecase,
): ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun get(page: Pageable) {
        viewModelScope.launch {
            usecase(
                ExplorePostsUsecase.Parameter(
                    criteria = Criteria.Content(sort = Sort.TREND),
                    page = page
                )
            )
                .catch { mutableState.tryEmit(State.Error(it)) }
                .onStart { mutableState.tryEmit(State.Loading) }
                .cachedIn(viewModelScope)
                .apply {
                    collectLatest {
                        mutableState.tryEmit(State.Success(this))
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

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val content: Flow<PagingData<UiPost>>) : State
        data class Error(val error: Throwable) : State
    }
}
