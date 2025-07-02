package eu.peernetwork.blog.ui.timeline.video

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.usecase.BackgroundUsecase
import eu.peernetwork.blog.ui.usecase.UserVideosUsecase
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.blog.domain.model.Relation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    private val usecase: UserVideosUsecase,
    interactor: ThumbnailInteractor,
    private val backgroundUsecase: BackgroundUsecase,
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    val thumbnail: StateFlow<Map<String, Bitmap?>> = interactor.observe().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap()
        )

    var lastRelation: Relation? = null

    fun load(
        page: Pageable,
        relation: Relation = Relation.NONE,
        criteria: Criteria? = null
    ) {
        lastRelation = relation
        viewModelScope.launch {
            usecase(
                UserVideosUsecase.Parameter(
                    relation = relation,
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

    fun sync(
        items: List<UiVideo>,
        type: UiMimeType,
        width: Int,
        position: Int,
        limit: Int
    ) {
        viewModelScope.launch {
            val end = if (items.size < limit) {
                items.size
            } else {
                limit
            }
            items.subList(position, end).asFlow().collect {
                backgroundUsecase(
                    BackgroundUsecase.Parameter(it.media, type, width, it.aspectRatio)
                )
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
