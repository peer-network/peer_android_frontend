package eu.peernetwork.media.ui.renderer

import android.content.Context
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import eu.peernetwork.media.core.renderer.ImageView
import javax.inject.Inject

class ImageViewDelegate @Inject constructor(
    private val context: Context
) : ImageView {
    @Composable
    override fun invoke(modifier: Modifier, spec: ImageView.Spec) {
        val width = with(LocalDensity.current) { 640.dp.roundToPx() }
        val ratio = remember { derivedStateOf {
            spec.property?.resolution?.let { (it.first.toFloat() / it.second.toFloat()) } ?: 1f
        } }
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(spec.url)
                .size(width)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(ratio.value),
        )
    }
}
