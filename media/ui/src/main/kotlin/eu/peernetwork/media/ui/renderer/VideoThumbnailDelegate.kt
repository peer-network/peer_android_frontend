package eu.peernetwork.media.ui.renderer

import android.graphics.SurfaceTexture
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.media.core.renderer.VideoThumbnail
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import androidx.core.view.isVisible
import eu.peernetwork.media.core.interactor.VideoInteractor
import eu.peernetwork.media.ui.compose.VolumeControl
import eu.peernetwork.media.ui.core.MediaPlayer
import eu.peernetwork.media.ui.view.TextureViewWrapper
import kotlinx.coroutines.launch

class VideoThumbnailDelegate @Inject constructor(
    private val interactor: VideoInteractor
) : VideoThumbnail {
    private val media = (interactor as MediaPlayer)

    @Composable
    @OptIn(FlowPreview::class)
    override fun invoke(
        modifier: Modifier,
        spec: VideoThumbnail.Spec
    ) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val scope = rememberCoroutineScope()
        val surfaceView = remember { TextureViewWrapper(TextureView(context)) }
        val dimension = media.observer.collectAsStateWithLifecycle()
        var mute = media.mute().collectAsStateWithLifecycle(media.player().isDeviceMuted)
        val callback = remember {
            object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(
                    texture: SurfaceTexture,
                    p1: Int,
                    p2: Int
                ) {
                    if (!media.expandedMode()) {
                        interactor.attach(texture, spec.url)
                    }
                }

                override fun onSurfaceTextureSizeChanged(
                    p0: SurfaceTexture,
                    p1: Int,
                    p2: Int
                ) {}

                override fun onSurfaceTextureDestroyed(texture: SurfaceTexture): Boolean {
                    interactor.detach(texture)
                    return true
                }

                override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {}
            }
        }
        val observer = remember {
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    surfaceView.attachCallback(callback)
                } else if (event == Lifecycle.Event.ON_STOP && !spec.isPlaying) {
                    surfaceView.clearCallback()
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
                factory = { surfaceView.view },
                update = {
                    it.visibility = if (spec.isPlaying) {
                        View.VISIBLE
                    } else {
                        View.INVISIBLE
                    }
                    it.alpha = 0f
                    if (it.isVisible) {
                        val dimen = dimension.value[spec.url] ?: spec.ratio
                        if (dimen > 1) {
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
                VolumeControl(mute) { scope.launch { interactor.mute(it) } }
            }
        }
        LaunchedEffect(spec.isPlaying) {
            lifecycleOwner.lifecycle.addObserver(observer)
        }
        LaunchedEffect(mute.value) {
            media.player().volume = if (mute.value) {
                1f
            } else {
                0f
            }
        }
    }
}
