package eu.peernetwork.media.ui.selector.directory

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.model.UiDirectory
import eu.peernetwork.media.ui.usecase.PhotoDirectoryUsecase
import eu.peernetwork.media.ui.usecase.VideoDirectoryUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class DirectoryViewModel @Inject constructor(
    private val usecase: PhotoDirectoryUsecase,
    private val videoUsecase: VideoDirectoryUsecase,
    private val interactor: ThumbnailInteractor
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    val thumbnail: StateFlow<Map<String, Bitmap?>> = interactor.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap()
        )

    fun initialize(type: UiMimeType) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            try {
                if (type == UiMimeType.Video) {
                    mutableState.tryEmit(State.Success(videoUsecase()))
                } else {
                    mutableState.tryEmit(State.Success(usecase()))
                }
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    fun sync(type: UiMimeType, position: Int, limit: Int) {
        viewModelScope.launch {
            (state.value as? State.Success?)?.directories?.let {
                val end = if (it.size < limit) {
                    it.size
                } else {
                    limit
                }
                it.toList().subList(position, end).asFlow()
                    .map { interactor.load(it.thumbnail, type, Pair(250f, 250f)) }
                    .collect { interactor.invalidate() }
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val directories: Set<UiDirectory>) : State
        data class Error(val error: Throwable) : State
    }
}
