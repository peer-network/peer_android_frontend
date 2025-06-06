package eu.peernetwork.social.ui.followings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.ui.model.UiMember
import eu.peernetwork.social.ui.usecase.FollowingPagingUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class FollowingsViewModel @Inject constructor(
    private val followingsUsecase: FollowingPagingUsecase
): ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)
    val state: StateFlow<State> = mutableState.asStateFlow()

    fun followers(userId: String, pageable: Pageable) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            followingsUsecase(
                FollowingPagingUsecase.Parameter(userId, pageable)
            ).catch { mutableState.tryEmit(State.Error(it)) }
                .onStart { mutableState.tryEmit(State.Loading) }
                .cachedIn(viewModelScope)
                .apply {
                    collectLatest { mutableState.tryEmit(State.Success(this)) }
                }
        }
    }

    fun reset() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Empty)
        }
    }

    sealed interface State {
        data object Empty: State
        data object Loading: State
        data class Success(val content: Flow<PagingData<UiMember>>): State
        data class Error(val error: Throwable): State
    }
}