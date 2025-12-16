package eu.peernetwork.social.ui.block

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.ui.model.UiBlock
import eu.peernetwork.social.ui.usecase.BlockListPagingUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class BlockListViewModel @Inject constructor(
    private val usecase: BlockListPagingUsecase
): ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Loading)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun blockList(userId: String, pageable: Pageable) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            usecase(
                BlockListPagingUsecase.Parameter(userId, pageable)
            ).catch { mutableState.tryEmit(State.Error(it)) }
                .onStart { mutableState.tryEmit(State.Loading) }
                .cachedIn(viewModelScope)
                .apply {
                    collectLatest {
                        mutableState.tryEmit(State.Success(this))
                    }
                }
        }
    }

    sealed interface State {
        data object Empty: State
        data object Loading: State
        data class Success(val content: Flow<PagingData<UiBlock>>): State
        data class Error(val error: Throwable): State
    }
}