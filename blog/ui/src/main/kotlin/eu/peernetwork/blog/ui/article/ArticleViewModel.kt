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
    private val mutableState = MutableStateFlow<Map<Int, State>>(emptyMap())

    val states: StateFlow<Map<Int, State>> = mutableState.asStateFlow()

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
        mutableState.update { it + (tab to state) }
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
