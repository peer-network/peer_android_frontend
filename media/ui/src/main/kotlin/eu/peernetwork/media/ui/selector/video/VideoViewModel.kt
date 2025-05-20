package eu.peernetwork.media.ui.selector.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.media.core.model.UiFile
import eu.peernetwork.media.ui.usecase.VideoUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    private val usecase: VideoUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun initialize(directory: String?) {
        viewModelScope.launch {
            try {
                mutableState.tryEmit(State.Loading)
                mutableState.tryEmit(State.Success(usecase(directory)))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val videos: List<UiFile>): State
        data class Error(val error: Throwable): State
    }
}
