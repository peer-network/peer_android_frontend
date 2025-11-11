package eu.peernetwork.blog.ui.advert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.usecase.FeedAdvertUsecase
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

class AdvertViewModel @Inject constructor(
    private val usecase: FeedAdvertUsecase
) : ViewModel() {
    private val _states = MutableStateFlow<Map<Int, State>>(emptyMap())

    val states: StateFlow<Map<Int, State>> = _states.asStateFlow()

    private fun updateState(tab: Int, state: State) {
        _states.update { it + (tab to state) }
    }

    operator fun invoke(types: Set<Content.Type>, page: Pageable) {
        val key = types.hashCode()
        viewModelScope.launch {
            usecase(
                FeedAdvertUsecase.Parameter(
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

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val content: Flow<PagingData<UiPost>>) : State
        data class Error(val error: Throwable) : State
    }
}
