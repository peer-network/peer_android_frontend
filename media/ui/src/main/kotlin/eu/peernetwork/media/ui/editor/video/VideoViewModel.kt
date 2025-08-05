package eu.peernetwork.media.ui.editor.video

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.model.UiMetadata
import eu.peernetwork.media.ui.usecase.CoverUsecase
import eu.peernetwork.media.ui.usecase.MetadataRetrieverUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    private val coverUsecase: CoverUsecase,
    private val metadataRetrieverUsecase: MetadataRetrieverUsecase,
    private val interactor: ThumbnailInteractor,
) : ViewModel() {
    private val requests = ConcurrentHashMap.newKeySet<String>()

    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    val thumbnail: StateFlow<Map<String, Bitmap?>> = interactor.observe().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyMap()
    )

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

    fun sync(url: String, name: String, timestamp: Long) {
        if (requests.contains(name)) {
            return
        }
        requests.add(name)
        viewModelScope.launch {
            metadataRetrieverUsecase(
                MetadataRetrieverUsecase.Parameter(
                    url = url,
                    type = UiMimeType.Video,
                    frame = timestamp
                )
            )?.bitmap?.let {
                interactor.save(name, it)
                interactor.invalidate()
            }
            requests.remove(name)
        }
    }

    fun background(url: String, width: Int, height: Int) {
        viewModelScope.launch {
            coverUsecase(
                CoverUsecase.Parameter(
                    url = url,
                    type = UiMimeType.Video,
                    width = width,
                    height = height
                )
            )
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val data: UiMetadata) : State
        data class Error(val error: Throwable) : State
    }
}
