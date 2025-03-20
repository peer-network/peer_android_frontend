package eu.peernetwork.social.ui.content.music

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun MusicScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    Text(
        text = "Music page",
        modifier = Modifier.fillMaxSize()
            .padding(24.dp),
        textAlign = TextAlign.Center
    )
}
