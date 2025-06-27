package eu.peernetwork.media.ui.selector.photo

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiFile
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.usecase.PhotoUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class PhotoViewModel @Inject constructor(
    private val usecase: PhotoUsecase,
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

    fun sync(type: UiMimeType, start: Int, limit: Int) {
        viewModelScope.launch {
            (state.value as? State.Success)?.photos?.let { list ->
                val end = if (list.size < limit + 1) list.size else limit + 1

                if(start <= end) {
                    list.subList(start, end)
                        .asFlow()
                        .map { photo ->
                            interactor.load(photo.thumbnail, type, Pair(300f, 300f))
                        }
                        .collect {
                            interactor.invalidate()
                        }
                }
            }
        }
    }

    fun preloadPreview(key: String, px: Int, type: UiMimeType) =
        viewModelScope.launch {
            interactor.load(key, type, Pair(px.toFloat(), px.toFloat()))
            interactor.invalidate()
        }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val photos: List<UiFile>) : State
        data class Error(val error: Throwable) : State
    }
}
