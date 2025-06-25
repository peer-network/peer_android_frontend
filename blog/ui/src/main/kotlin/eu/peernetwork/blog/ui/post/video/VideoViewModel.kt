package eu.peernetwork.blog.ui.post.video

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    private val usecase: AuthorVideoUsecase,
    interactor: ThumbnailInteractor,
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

    fun load(
        author: String,
        page: Pageable
    ) {
        viewModelScope.launch {
            usecase(
                AuthorVideoUsecase.Parameter(
                    author = author,
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

    fun thumbnail(url: String, type: UiMimeType, width: Int, ratio: Float) {
        viewModelScope.launch {
            try {
                backgroundUsecase(BackgroundUsecase.Parameter(url, type, width, ratio))
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val content: Flow<PagingData<UiVideo>>) : State
        data class Error(val error: Throwable) : State
    }
}
