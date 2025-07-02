package eu.peernetwork.core.ui.design.compose

import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

@Composable
fun DesignThumbnail(
    thumbnail: String,
    bitmap: Bitmap?,
    contentScale: ContentScale = ContentScale.Crop,
    onRefresh: (String) -> Unit,
) {
    val handleOnRefresh by rememberUpdatedState(onRefresh)
    DesignThumbnail(bitmap, contentScale)
    LaunchedEffect(thumbnail) {
        if (bitmap == null) {
            handleOnRefresh(thumbnail)
        }
    }
}

@Composable
fun DesignThumbnail(
    bitmap: Bitmap?,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val image = remember(bitmap) { bitmap?.asImageBitmap() }
    Box(modifier = Modifier.fillMaxSize()) {
        Crossfade(image) { target ->
            if (target != null) {
                Image(
                    bitmap = target,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = contentScale
                )
            }
        }
    }
}
