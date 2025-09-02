package eu.peernetwork.media.ui.renderer

import android.view.TextureView
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.media.core.renderer.VideoThumbnail
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import androidx.core.view.isVisible
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import eu.peernetwork.media.core.interactor.VideoInteractor
import eu.peernetwork.media.ui.compose.VolumeControl
import eu.peernetwork.media.ui.core.MediaSession
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class VideoThumbnailDelegate @Inject constructor(
    private val interactor: VideoInteractor
) : VideoThumbnail {
    private val session = (interactor as MediaSession)

    @Composable
    @OptIn(FlowPreview::class)
    override fun invoke(
        modifier: Modifier,
        spec: VideoThumbnail.Spec
    ) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val player = remember { session.exoPlayer() }
        val surfaceView = remember { TextureView(context) }
        val dimension = session.observer.collectAsStateWithLifecycle()
        val mute = session.mute().collectAsStateWithLifecycle(session.exoPlayer().isDeviceMuted)
        val listener = remember {
            object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    surfaceView.surfaceTexture?.let {
                        interactor.attach(it, spec.url)
                    }
                }
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(spec.ratio)
        ) {
            AndroidView(
                factory = { surfaceView },
                update = {
                    it.visibility = if (spec.isPlaying.value) {
                        View.VISIBLE
                    } else {
                        View.INVISIBLE
                    }
                    it.alpha = 0f
                    if (it.isVisible) {
                        val dimen = dimension.value[spec.url] ?: spec.ratio
                        if (dimen >= 1) {
                            val width = it.measuredWidth
                            it.layoutParams = it.layoutParams.apply {
                                this.width = width
                                this.height = (width / dimen).toInt()
                            }
                        } else {
                            val height = it.measuredHeight
                            it.layoutParams = it.layoutParams.apply {
                                this.width = (height * dimen).toInt()
                                this.height = height
                            }
                        }
                        it.alpha = dimension.value[spec.url]?.let { 1f } ?: 0f
                    }
                },
                modifier = Modifier.wrapContentSize()
            )
            Box(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)) {
                VolumeControl(mute) { scope.launch { session.mute(it) } }
            }
        }
        LaunchedEffect(Unit) {
            snapshotFlow { spec.isPlaying.value }
                .distinctUntilChanged()
                .debounce(300)
                .collect { playing ->
                    if (playing) {
                        surfaceView.surfaceTexture?.let {
                            interactor.attach(it, spec.url)
                            player.addListener(listener)
                            player.play()
                        }
                    } else {
                        surfaceView.surfaceTexture?.let {
                            interactor.detach(it)
                            player.removeListener(listener)
                            player.pause()
                        }
                    }
                }
        }
        LaunchedEffect(Unit) {
            snapshotFlow { mute.value }
                .distinctUntilChanged()
                .collect { muted ->
                    session.exoPlayer().volume = if (muted) 1f else 0f
                }
        }
    }
}
