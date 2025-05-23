package eu.peernetwork.media.ui.thumbnail

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.model.UiMimeType
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ThumbnailScreen(
    type: UiMimeType,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (StateFlow<Map<String, Bitmap?>>, (String) -> Unit) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Thumbnail.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ThumbnailViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val updatedContent by rememberUpdatedState(content)
    updatedContent(viewModel.thumbnails) { viewModel.initialize(it, type) }
}

@Composable
fun ThumbnailScreen(
    thumbnail: String,
    state: StateFlow<Map<String, Bitmap?>>,
    onRefresh: (String) -> Unit
) {
    val images by state.collectAsStateWithLifecycle()
    images[thumbnail]?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(.7f),
            contentScale = ContentScale.Crop
        )
    } ?: Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant))
    LaunchedEffect(thumbnail) {
        onRefresh(thumbnail)
    }
}
