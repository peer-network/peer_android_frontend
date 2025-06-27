package eu.peernetwork.media.ui.editor.video

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.media.core.interactor.ExtractThumbnailsInteractor
import eu.peernetwork.media.core.usecase.VideoTrimUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val trimUsecase: VideoTrimUsecase,
    private val thumbs: ExtractThumbnailsInteractor
) : ViewModel() {

    private val _mutableState = MutableStateFlow<State>(State.Idle)
    val mutableState: StateFlow<State> = _mutableState.asStateFlow()

    private var currentTrimStart = 0L
    private var currentTrimEnd = 0L

    fun setTrim(start: Long, end: Long) {
        currentTrimStart = start
        currentTrimEnd = end
        (_mutableState.value as? State.Ready)?.let { ready ->
            _mutableState.value = ready.copy(trimStart = start, trimEnd = end)
        }
    }

    fun loadThumbs(
        path: String,
        frameSlots: Int = 12,
        thumbWidth: Int = 160
    ) = viewModelScope.launch(dispatcher.main) {
        _mutableState.value = State.Loading

        val result = runCatching {
            withContext(dispatcher.io) {
                thumbs.extract(path, frameSlots, thumbWidth)
            }
        }

        result.fold(
            onSuccess = { (durationMs, frames) ->
                currentTrimStart = 0L
                currentTrimEnd = durationMs
                _mutableState.value = State.Ready(
                    durationMs = durationMs,
                    trimStart = 0L,
                    trimEnd = durationMs,
                    frames = frames
                )
            },
            onFailure = { _mutableState.value = State.Failure(it) }
        )
    }

    fun confirmTrim(input: String, cacheDir: File) = viewModelScope.launch {
        val ready = _mutableState.value as? State.Ready ?: return@launch
        _mutableState.value = State.Exporting(ready, pct = 0)

        val param = VideoTrimUsecase.Parameter(
            input    = input,
            cacheDir = cacheDir,
            startMs  = currentTrimStart,
            endMs    = currentTrimEnd
        )

        trimUsecase(param).collect { res ->
            when (res) {
                is VideoTrimUsecase.Result.Progress ->
                    _mutableState.value = State.Exporting(ready, res.pct)

                is VideoTrimUsecase.Result.Success  ->
                    _mutableState.value = State.Complete(ready, res.file)

                is VideoTrimUsecase.Result.Failure  ->
                    _mutableState.value = State.Failure(res.cause)
            }
        }
    }

    fun clearExport() {
        _mutableState.value = State.Idle
    }

    sealed interface State {
        data object Idle : State
        data object Loading : State

        data class Ready(
            val durationMs: Long,
            val trimStart: Long,
            val trimEnd: Long,
            val frames: List<Bitmap>
        ) : State

        data class Exporting(
            val base: Ready,
            val pct: Int
        ) : State

        data class Complete(
            val base: Ready,
            val file: File
        ) : State

        data class Failure(val cause: Throwable) : State
    }
}
