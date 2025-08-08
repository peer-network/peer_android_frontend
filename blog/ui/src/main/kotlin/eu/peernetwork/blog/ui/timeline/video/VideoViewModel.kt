package eu.peernetwork.blog.ui.timeline.video

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.usecase.UserVideosUsecase
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.media.core.viewmodel.MediaViewModel
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
    private val usecase: UserVideosUsecase,
    interactor: ThumbnailInteractor
) : MediaViewModel(interactor) {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    var lastCategory: Category? = null

    fun load(
        page: Pageable,
        category: Category = Category.ALL,
        criteria: Criteria? = null,
    ) {
        lastCategory = category
        viewModelScope.launch {
            usecase(
                UserVideosUsecase.Parameter(
                    category = category,
                    criteria = criteria,
                    page = page
                )
            )
                .catch { mutableState.tryEmit(State.Error(it)) }
                .onStart { mutableState.tryEmit(State.Loading) }
                .cachedIn(viewModelScope)
                .apply {
                    collectLatest { mutableState.tryEmit(State.Success(this)) }
                }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val data: Flow<PagingData<UiVideo>>) : State
        data class Error(val error: Throwable) : State
    }
}
