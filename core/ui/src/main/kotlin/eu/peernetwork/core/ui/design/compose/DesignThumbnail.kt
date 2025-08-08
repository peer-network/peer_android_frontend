package eu.peernetwork.core.ui.design.compose

import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun DesignThumbnail(
    thumbnail: String,
    bitmap: Bitmap?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    onLoad: (String) -> Unit,
) {
    val handleOnLoad by rememberUpdatedState(onLoad)
    val image = remember(bitmap) { bitmap?.asImageBitmap() }
    DesignThumbnail(image, modifier, contentScale)
    LaunchedEffect(thumbnail) {
        if (bitmap == null) {
            handleOnLoad(thumbnail)
        }
    }
}

@Composable
fun DesignThumbnail(
    enable: State<Boolean>,
    thumbnail: String,
    bitmap: Bitmap?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    onLoad: (String) -> Unit,
) {
    val handleOnLoad by rememberUpdatedState(onLoad)
    val image = remember(bitmap) { bitmap?.asImageBitmap() }
    val isVisible = remember { derivedStateOf { bitmap == null && enable.value } }
    DesignThumbnail(image, modifier, contentScale)
    LaunchedEffect(Unit) {
        snapshotFlow { isVisible.value }
            .distinctUntilChanged()
            .collectLatest {
                handleOnLoad(thumbnail)
            }
    }
}

@Composable
@OptIn(FlowPreview::class, ExperimentalAnimationApi::class)
fun DesignThumbnail(
    enable: State<Boolean>,
    thumbnail: String,
    bitmap: State<Bitmap?>,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    onLoad: (String) -> Unit,
) {
    val handleOnLoad by rememberUpdatedState(onLoad)
    val image = remember(bitmap.value) { bitmap.value?.asImageBitmap() }
    Box(modifier = modifier) {
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
    LaunchedEffect(Unit) {
        snapshotFlow { bitmap.value == null && enable.value }
            .distinctUntilChanged()
            .debounce(300)
            .collectLatest { visible ->
                if (visible) handleOnLoad(thumbnail)
            }
    }
}

@Composable
fun DesignThumbnail(
    bitmap: Bitmap?,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val image = remember(bitmap) { bitmap?.asImageBitmap() }
    DesignThumbnail(image, modifier = Modifier.fillMaxSize(), contentScale)
}

@Composable
fun DesignThumbnail(
    bitmap: Bitmap?,
    contentScale: ContentScale,
    modifier: Modifier = Modifier,
) {
    val image = remember(bitmap) { bitmap?.asImageBitmap() }
    DesignThumbnail(image, modifier, contentScale)
}

@Composable
fun DesignThumbnail(
    bitmap: ImageBitmap?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    Box(modifier = modifier) {
        Crossfade(bitmap) { target ->
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
