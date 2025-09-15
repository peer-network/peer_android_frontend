package eu.peernetwork.media.ui.interactor

import android.graphics.SurfaceTexture
import android.view.Surface
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import eu.peernetwork.media.core.interactor.VideoInteractor
import eu.peernetwork.persistence.domain.observable.ObservableBoolean
import eu.peernetwork.persistence.domain.publishable.PublishableBoolean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(UnstableApi::class)
class VideoInteractorDelegate @Inject constructor(
    private val player: ExoPlayer,
    private val observableBoolean: ObservableBoolean,
    private val publishableBoolean: PublishableBoolean
) : VideoInteractor, MediaInteractor {
    private var currentUrl: MediaItem? = null

    private var currentSurface: Surface? = null

    private val aspectRatios = ConcurrentHashMap<String, Float>()

    private val mutableAspectRatios = MutableStateFlow<Map<String, Float>>(emptyMap())

    override val observer: StateFlow<Map<String, Float>> = mutableAspectRatios.asStateFlow()

    private val listener = object : Player.Listener {
        override fun onVideoSizeChanged(videoSize: VideoSize) {
            val width = videoSize.width
            val height = videoSize.height
            if (width > 0 && height > 0) {
                currentUrl?.let {
                    aspectRatios[it.mediaId] = width.toFloat() / height
                    mutableAspectRatios.tryEmit(aspectRatios.toMap())
                }
            }
        }
    }

    override fun mute(): Flow<Boolean> {
        return observableBoolean(VOLUME).map { it == true }
    }

    override suspend fun mute(enable: Boolean) {
        publishableBoolean(VOLUME, enable)
    }

    override fun exoPlayer(): ExoPlayer = player

    override fun attach(texture: SurfaceTexture, url: String) {
        val item = MediaItem.Builder().setUri(url).setMediaId(url).build()
        val surface = Surface(texture)
        player.setMediaItem(item)
        player.prepare()
        currentUrl = item
        player.setVideoSurface(surface)
        currentSurface = surface
        player.playWhenReady = true
        player.addListener(listener)
    }

    override fun detach(texture: SurfaceTexture) {
        val surface = Surface(texture)
        if (currentSurface === surface) {
            player.playWhenReady = false
            player.removeListener(listener)
            player.clearVideoSurface(surface)
            currentSurface = null
        }
    }

    override fun dispose() {
        aspectRatios.clear()
        player.release()
    }

    data class State(
        val url: MediaItem,
        val texture: Surface
    )

    private companion object {
        const val VOLUME = "eu.peernetwork.media.ui.interactor.VOLUME"
    }
}
