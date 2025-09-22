package eu.peernetwork.blog.ui.interaction.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.blog.ui.usecase.InteractorUsecase
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

class ListingViewModel @Inject constructor(
    private val usecase: InteractorUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun load(
        id: String,
        engagement: Engagement.Content,
        page: Pageable
    ) {
        viewModelScope.launch {
            usecase(
                InteractorUsecase.Parameter(
                    id = id,
                    engagement = engagement,
                    page = page,
                )
            ).catch { mutableState.tryEmit(State.Error(it)) }
                .onStart { mutableState.tryEmit(State.Loading) }
                .cachedIn(viewModelScope)
                .apply {
                    collectLatest { mutableState.tryEmit(State.Success(this)) }
                }
        }
    }

    sealed interface State {
        data object Empty: State
        data object Loading: State
        data class Success(val content: Flow<PagingData<UiAuthor>>): State
        data class Error(val error: Throwable): State
    }
}
