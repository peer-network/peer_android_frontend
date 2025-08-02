package eu.peernetwork.media.ui.editor.video

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.model.UiMetadata
import eu.peernetwork.media.ui.usecase.MetadataRetrieverUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    private val usecase: MetadataRetrieverUsecase,
    private val interactor: ThumbnailInteractor,
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    val thumbnail: StateFlow<Map<String, Bitmap?>> = interactor.observe().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyMap()
    )

    fun get(url: String) {
        viewModelScope.launch {
            try {
                mutableState.tryEmit(State.Loading)
                val data = usecase(
                    MetadataRetrieverUsecase.Parameter(
                        url = url,
                        type = UiMimeType.Video
                    )
                ) ?: throw NoContentException()
                mutableState.tryEmit(State.Success(data))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    fun sync(url: String, name: String, timestamp: Long) {
        viewModelScope.launch {
            usecase(
                MetadataRetrieverUsecase.Parameter(
                    url,
                    UiMimeType.Video,
                    timestamp
                )
            )?.bitmap?.let {
                interactor.save(name, it)
                interactor.invalidate()
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
        data class Success(val data: UiMetadata) : State
        data class Error(val error: Throwable) : State
    }
}
