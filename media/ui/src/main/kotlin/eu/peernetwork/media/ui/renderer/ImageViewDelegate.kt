package eu.peernetwork.media.ui.renderer

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.ui.renderer.BlurTransformer
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import javax.inject.Inject

class ImageViewDelegate @Inject constructor(
    private val context: Context,
    private val transformer: BlurTransformer
) : ImageView {
    @Composable
    override fun invoke(modifier: Modifier, spec: ImageView.Spec) {
        val density = LocalDensity.current
        val boxModifier = spec.ratio?.let {
            Modifier.fillMaxWidth().aspectRatio(it)
        } ?: Modifier.fillMaxSize()
        val imageRequest = remember {
            ImageRequest.Builder(context)
                .data(spec.url)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .apply {
                    if (spec.blur != 0f) {
                        transformations(transformer)
                    } else {
                        size(with(density) { spec.width.dp.roundToPx() })
                    }
                }.build()
        }
        if (spec.zoomable) {
            val zoomState = rememberZoomState()
            LaunchedEffect(spec.url) { zoomState.reset() }
            Box(
                modifier = boxModifier
                    .then(modifier)
                    .zoomable(zoomState = zoomState)
            ) {
                AsyncImage(
                    model = imageRequest,
                    contentDescription = null,
                    contentScale = spec.contentScale,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                contentScale = spec.contentScale,
                modifier = boxModifier.then(modifier)
            )
        }
    }
}
