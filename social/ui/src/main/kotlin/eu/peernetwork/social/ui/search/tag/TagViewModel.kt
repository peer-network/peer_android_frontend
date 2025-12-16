package eu.peernetwork.social.ui.search.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.ui.model.UiTag
import eu.peernetwork.social.ui.usecase.TagUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class TagViewModel @Inject constructor(private val usecase: TagUsecase) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = _state.asStateFlow()

    fun search(tag: String, page: Pageable) {
        viewModelScope.launch {
            _state.tryEmit(State.Loading)
            usecase(
                TagUsecase.Parameter(
                    tag = tag,
                    page = page
                )
            ).catch { _state.tryEmit(State.Error(it)) }
                .onStart { _state.tryEmit(State.Loading) }
                .cachedIn(viewModelScope)
                .apply {
                    collectLatest { _state.tryEmit(State.Success(tag, this)) }
                }
        }
    }

    fun reset() {
        viewModelScope.launch {
            _state.tryEmit(State.Empty)
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(
            val tag: String,
            val content: Flow<PagingData<UiTag>>
        ) : State
        data class Error(val error: Throwable) : State
    }
}
