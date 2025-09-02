package eu.peernetwork.social.ui.search.title

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.ui.model.UiPost
import eu.peernetwork.social.ui.usecase.TitleUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class TitleViewModel @Inject constructor(private val usecase: TitleUsecase) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun search(title: String, page: Pageable) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            usecase(
                TitleUsecase.Parameter(
                    title = title,
                    page = page
                )
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
        data object Empty : State
        data object Loading : State
        data class Success(val content: Flow<PagingData<UiPost>>) : State
        data class Error(val error: Throwable) : State
    }
}
