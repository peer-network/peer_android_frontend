package eu.peernetwork.media.ui.interactor

import android.graphics.SurfaceTexture
import android.view.Surface
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import eu.peernetwork.media.core.interactor.VideoInteractor
import eu.peernetwork.media.ui.provider.MediaPlayer

@OptIn(UnstableApi::class)
class VideoInteractorDelegate(private val player: ExoPlayer) : VideoInteractor, MediaPlayer {
    private var currentUrl: String? = null

    private var currentSurface: SurfaceTexture? = null

    override fun player(): ExoPlayer = player

    override fun attach(texture: SurfaceTexture, url: String) {
        if (currentUrl != url) {
            player.setMediaItem(MediaItem.fromUri(url))
            player.prepare()
            currentUrl = url
        }
        if (currentSurface !== texture) {
            player.setVideoSurface(Surface(texture))
            currentSurface = texture
        }
        player.playWhenReady = true
    }

    override fun detach(texture: SurfaceTexture) {
        if (currentSurface === texture) {
            player.playWhenReady = false
            player.clearVideoSurface(Surface(texture))
            currentSurface = null
        }
    }

    override fun dispose() {
        player.release()
    }
}
