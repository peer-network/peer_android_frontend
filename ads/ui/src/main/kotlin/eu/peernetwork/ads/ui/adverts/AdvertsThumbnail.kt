package eu.peernetwork.ads.ui.adverts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import eu.peernetwork.media.core.renderer.ImageView

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun AdvertsThumbnail(
    url: String,
    component: Adverts.Component
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val ratio = maxWidth.value / maxHeight.value
        component.imageView()(
            modifier = Modifier,
            spec = ImageView.Spec(
                url = url,
                ratio = ratio,
                contentScale = ContentScale.Crop
            )
        )
    }
}
