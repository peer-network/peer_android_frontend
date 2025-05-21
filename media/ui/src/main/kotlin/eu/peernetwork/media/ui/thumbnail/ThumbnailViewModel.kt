package eu.peernetwork.media.ui.thumbnail

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.usecase.ThumbnailUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class ThumbnailViewModel @Inject constructor(
    private val usecase: ThumbnailUsecase
) : ViewModel() {
    private val cache = ConcurrentHashMap<String, Bitmap?>()

    private val mutableThumbnails = MutableStateFlow<Map<String, Bitmap?>>(emptyMap())

    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    val thumbnails: StateFlow<Map<String, Bitmap?>> = mutableThumbnails.asStateFlow()

    fun initialize(thumbnail: String, type: UiMimeType) {
        viewModelScope.launch {
            try {
                mutableState.tryEmit(State.Loading)
                if (cache[thumbnail] != null) {
                    mutableThumbnails.tryEmit(cache.toMap())
                    mutableState.tryEmit(State.Success(thumbnail, cache[thumbnail]))
                } else {
                    val bitmap = usecase(ThumbnailUsecase.Parameter(thumbnail, type))
                    cache[thumbnail] = bitmap
                    mutableThumbnails.tryEmit(cache.toMap())
                    mutableState.tryEmit(State.Success(thumbnail, bitmap))
                }
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val thumbnail: String, val bitmap: Bitmap?) : State
        data class Error(val error: Throwable) : State
    }
}
