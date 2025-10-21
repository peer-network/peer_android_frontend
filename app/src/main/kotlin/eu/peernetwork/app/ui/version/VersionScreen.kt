package eu.peernetwork.app.ui.version

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiViewModelStore

@Composable
fun VersionScreen(
    version: String,
    versionCode: Int,
    provider: UiComponentProvider,
    viewModelStore: UiViewModelStore,
) {
    Text("Version: $version ($versionCode)")
}
