package eu.peernetwork.media.ui.selector.directory

import androidx.lifecycle.viewModelScope
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.model.UiDirectory
import eu.peernetwork.media.core.viewmodel.MediaViewModel
import eu.peernetwork.media.ui.usecase.AudioDirectoryUsecase
import eu.peernetwork.media.ui.usecase.PhotoDirectoryUsecase
import eu.peernetwork.media.ui.usecase.VideoDirectoryUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DirectoryViewModel @Inject constructor(
    private val usecase: PhotoDirectoryUsecase,
    private val videoUsecase: VideoDirectoryUsecase,
    private val audioUsecase: AudioDirectoryUsecase,
    interactor: ThumbnailInteractor
) : MediaViewModel(interactor){
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun initialize(type: UiMimeType) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            try {
                if (type == UiMimeType.Video) {
                    mutableState.tryEmit(State.Success(videoUsecase()))
                } else if (type == UiMimeType.Photo){
                    mutableState.tryEmit(State.Success(usecase()))
                } else {
                    mutableState.tryEmit(State.Success(audioUsecase()))
                }
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
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
