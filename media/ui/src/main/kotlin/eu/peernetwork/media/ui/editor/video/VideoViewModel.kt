package eu.peernetwork.media.ui.editor.video

import androidx.lifecycle.viewModelScope
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.model.UiMediaProperty
import eu.peernetwork.media.core.viewmodel.MediaViewModel
import eu.peernetwork.media.ui.usecase.MetadataRetrieverUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    private val metadataRetrieverUsecase: MetadataRetrieverUsecase,
    interactor: ThumbnailInteractor,
) : MediaViewModel(interactor) {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    var currentMedia: String? = null

    fun get(url: String) {
        if (currentMedia == url && mutableState.value is State.Success) {
            return
        }
        currentMedia = url
        viewModelScope.launch {
            try {
                mutableState.tryEmit(State.Loading)
                val data = metadataRetrieverUsecase(
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

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val data: UiMediaProperty) : State
        data class Error(val error: Throwable) : State
    }
}
