package eu.peernetwork.media.ui.renderer

import android.content.Context
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.ui.core.BlurTransformer
import javax.inject.Inject

class ImageViewDelegate @Inject constructor(
    private val context: Context,
    private val transformer: BlurTransformer
) : ImageView {
    @Composable
    override fun invoke(modifier: Modifier, spec: ImageView.Spec) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(spec.url)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .apply {
                    if (spec.blur != 0f) {
                        transformations(transformer)
                    } else {
                        size(with(LocalDensity.current) { spec.width.dp.roundToPx() })
                    }
                }
                .build(),
            contentDescription = null,
            contentScale = spec.contentScale,
            modifier = Modifier
                .then(spec.ratio?.let { Modifier.fillMaxWidth()
                    .aspectRatio(it) }
                    ?: Modifier.fillMaxSize())
                .then(modifier),
        )
    }
}
