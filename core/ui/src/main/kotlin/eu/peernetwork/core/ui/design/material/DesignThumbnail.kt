package eu.peernetwork.core.ui.design.material

import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
@OptIn(FlowPreview::class, ExperimentalAnimationApi::class)
fun DesignThumbnail(
    thumbnail: String,
    bitmap: State<Bitmap?>,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    onLoad: (String) -> Unit,
) {
    DesignThumbnail(
        enable = remember { mutableStateOf(true) },
        thumbnail = thumbnail,
        bitmap = bitmap,
        modifier = modifier,
        contentScale = contentScale,
        onLoad = onLoad
    )
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
