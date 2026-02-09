package eu.peernetwork.ads.ui.article

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.feature.ads.ui.R
import eu.peernetwork.media.core.renderer.ImageView

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun ArticleMedia(
    url: String,
    component: Article.Component
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
        Image(
            painter = painterResource(R.drawable.ic_pinned),
            contentDescription = stringResource(R.string.pin_label),
            modifier = Modifier.padding(6.dp)
                .size(20.dp)
        )
    }
}
