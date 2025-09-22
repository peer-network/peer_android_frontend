package eu.peernetwork.blog.ui.feed.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.ui.model.UiPost
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
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostViewModel @Inject constructor(
    private val usecase: FeedUsecase,
    private val viewUsecase: ViewUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun load(
        page: Pageable,
        category: Category = Category.NONE,
        criteria: Criteria? = null
    ) {
        viewModelScope.launch {
            usecase(
                FeedUsecase.Parameter(
                    category = category,
                    criteria = criteria,
                    page = page
                )
            )
                .catch {
                    mutableState.tryEmit(State.Error(it))
                }
                .onStart { mutableState.tryEmit(State.Loading) }
                .cachedIn(viewModelScope)
                .apply {
                    collectLatest {
                        mutableState.tryEmit(State.Success(
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