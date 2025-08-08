package eu.peernetwork.media.core.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.usecase.BackgroundUsecase
import eu.peernetwork.media.core.usecase.CoverUsecase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

abstract class MediaViewModel(
    private val interactor: ThumbnailInteractor,
    private val backgroundUsecase: BackgroundUsecase = BackgroundUsecase(interactor),
    private val coverUsecase: CoverUsecase = CoverUsecase(interactor)
) : ViewModel() {
    private val jobs = ConcurrentHashMap<String, Job>()

    val thumbnail: StateFlow<Map<String, Bitmap?>> = interactor.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap()
        )

    fun cover(url: String, width: Int, height: Int) {
        if (jobs.containsKey(url)) {
            return
        }
        jobs[url] = viewModelScope.launch {
            try {
                coverUsecase(
                    CoverUsecase.Parameter(
                        url = url,
                        type = UiMimeType.Video,
                        width = width,
                        height = height
                    )
                )
                interactor.invalidate()
            } catch (_: Throwable) { }
            jobs.remove(url)
        }
    }

    fun mediaThumbnail(media: String, type: UiMimeType = UiMimeType.Photo) {
        if (jobs.containsKey(media)) {
            return
        }
        jobs[media] = viewModelScope.launch {
            try {
                interactor.load(media, type, Pair(250f, 250f))
                interactor.invalidate()
            } catch (_: Throwable) {}
            jobs.remove(media)
        }
    }

    fun videoBackground(
        media: String,
        aspectRatio: Float,
        width: Int,
        height: Int = width,
        fit: Boolean = false
    ) {
        if (jobs.containsKey(media)) {
            return
        }
        jobs[media] = viewModelScope.launch {
            try {
                backgroundUsecase(
                    BackgroundUsecase.Parameter(
                        url = media,
                        type = UiMimeType.Video,
                        width = width,
                        height = height,
                        aspectRatio = aspectRatio,
                        fit = fit
                    )
                )
                interactor.invalidate()
            } catch (_: Throwable) { }
            jobs.remove(media)
        }
    }

    fun videoThumbnail(url: String, name: String, timestamp: Long) {
        if (jobs.containsKey(name)) {
            return
        }
        jobs[name] = viewModelScope.launch {
            try {
                interactor.get(
                    url = url,
                    type = UiMimeType.Video,
                    frame = timestamp
                )?.bitmap?.let {
                    interactor.save(name, it)
                    interactor.invalidate()
                }
                interactor.invalidate()
            } catch (_: Throwable) { }
            jobs.remove(name)
        }
    }

    fun reset() {
        jobs.forEach { it.value.cancel() }
        jobs.clear()
    }
}
