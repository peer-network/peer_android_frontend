package eu.peernetwork.blog.ui.post.video

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.peernetwork.blog.ui.mapper.query
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.usecase.AuthorVideoUsecase
import eu.peernetwork.blog.ui.usecase.BackgroundUsecase
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMimeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    private val usecase: AuthorVideoUsecase,
    private val interactor: ThumbnailInteractor,
    private val backgroundUsecase: BackgroundUsecase,
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    val thumbnail: StateFlow<Map<String, Bitmap?>> = interactor.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap()
        )

    fun load(author: String, mode: String?, page: Pageable) {
        viewModelScope.launch {
            usecase(
                AuthorVideoUsecase.Parameter(
                    author = author,
                    mode = mode,
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

    fun sync(
        items: List<UiVideo>,
        width: Int,
        start: Int,
        end: Int
    ) {
        viewModelScope.launch {
            val limit = if (items.size < end + 1) {
                items.size
            } else {
                end + 1
            }
            if (start <= limit) {
                items.subList(start, limit).asFlow()
                    .map {
                        backgroundUsecase(
                            BackgroundUsecase.Parameter(
                                it.media,
                                UiMimeType.Video,
                                width,
                                width,
                                it.aspectRatio
                            )
                        )
                    }.collect { interactor.invalidate() }
            }
        }
    }

    fun load(
        items: List<UiVideo>,
        width: Int,
        height: Int,
        position: Int
    ) {
        viewModelScope.launch {
            val item = items[position]
            backgroundUsecase(
                BackgroundUsecase.Parameter(
                    "${item.media}${UiMimeType.Video.query()}",
                    UiMimeType.Video,
                    width,
                    height,
                    item.aspectRatio,
                    true
                )
            )
            interactor.invalidate()
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val content: Flow<PagingData<UiVideo>>) : State
        data class Error(val error: Throwable) : State
    }
}
