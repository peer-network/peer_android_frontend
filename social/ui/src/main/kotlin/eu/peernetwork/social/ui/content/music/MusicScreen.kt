package eu.peernetwork.social.ui.content.music

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun MusicScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    Text("Music page")
}
