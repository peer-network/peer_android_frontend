package eu.peernetwork.media.ui.renderer

import android.content.Context
import android.graphics.SurfaceTexture
import android.view.TextureView
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import eu.peernetwork.media.core.provider.VideoProvider
import eu.peernetwork.media.core.renderer.VideoThumbnail
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class VideoThumbnailDelegate @Inject constructor(
    private val context: Context,
    provider: VideoProvider
) : VideoThumbnail {

    private val height: Int get() = context.resources.displayMetrics.heightPixels

    private val interactor = provider.timeline

    @Composable
    @OptIn(FlowPreview::class)
    override fun invoke(
        modifier: Modifier,
        spec: VideoThumbnail.Spec
    ) {
        val context = LocalContext.current
        val isVisible = remember { mutableStateOf(false) }
        val shouldPlay = remember { mutableStateOf(false) }
        LaunchedEffect(isVisible.value) {
            snapshotFlow { isVisible.value }
                .distinctUntilChanged()
                .debounce(300)
                .collectLatest { visible ->
                    shouldPlay.value = visible
                }
        }
        val ratio = remember {
            derivedStateOf {
                spec.resolution?.let { (width, height) ->
                    height.toFloat() / width.toFloat()
                } ?: 1f
            }
        }
        val surfaceView = remember(spec.url) { SurfaceViewWrapper(TextureView(context)) }
        DisposableEffect(spec.url) {
            val callback = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(
                    texture: SurfaceTexture,
                    p1: Int,
                    p2: Int
                ) {
                    if (shouldPlay.value) {
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
            surfaceView.attachCallback(callback)
            onDispose { surfaceView.clearCallback() }
        }
        Box(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(ratio.value)
                .onGloballyPositioned { coordinates ->
                    val rect = coordinates.boundsInRoot()
                    val threshold = height / 4
                    val isNowVisible = rect.top <= height - threshold && rect.bottom >= threshold
                    if (isNowVisible != isVisible.value) {
                        isVisible.value = isNowVisible
                    }
                }
                .background(Color.Black)
        ) {
            AndroidView(
                factory = { surfaceView.surfaceView },
                update = {
                    it.visibility = if (shouldPlay.value) View.VISIBLE else View.INVISIBLE
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
    class SurfaceViewWrapper(val surfaceView: TextureView) {
        private var callback: TextureView.SurfaceTextureListener? = null

        fun attachCallback(callback: TextureView.SurfaceTextureListener) {
            surfaceView.surfaceTextureListener = callback
            this.callback = callback
        }

        fun clearCallback() {
            callback?.let {
                surfaceView.surfaceTextureListener = null
                callback = null
            }
        }
    }
}
