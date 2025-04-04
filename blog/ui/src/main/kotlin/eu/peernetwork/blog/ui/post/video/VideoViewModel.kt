package eu.peernetwork.blog.ui.post.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.usecase.AuthorPostUsecase
import eu.peernetwork.blog.ui.usecase.AuthorVideoUsecase
import eu.peernetwork.core.common.model.Pageable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    private val usecase: AuthorVideoUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    private val observable = MutableStateFlow<Flow<PagingData<UiVideo>>?>(null)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun observe(): StateFlow<Flow<PagingData<UiVideo>>?> = observable

    fun load(author: String, page: Pageable) {
        viewModelScope.launch {
            usecase(
                AuthorVideoUsecase.Parameter(
                    author = author,
                    page = page
                )
            ).catch { mutableState.tryEmit(State.Error(it)) }
                .onStart { mutableState.tryEmit(State.Loading) }
                .cachedIn(viewModelScope)
                .apply { observable.tryEmit(this) }
                .collectLatest { mutableState.tryEmit(State.Success) }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data object Success : State
        data class Error(val error: Throwable) : State
    }
}
