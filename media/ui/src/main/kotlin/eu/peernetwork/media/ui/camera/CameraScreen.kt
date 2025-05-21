package eu.peernetwork.media.ui.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.ui.selector.photo.Photo

@Composable
fun CameraScreen(provider: UiComponentProvider) {
    val context = LocalContext.current
    remember {
        provider.builder(Photo.Builder::class.java).build(context)
    }
}
