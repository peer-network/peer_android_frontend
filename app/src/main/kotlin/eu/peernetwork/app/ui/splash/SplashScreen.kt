package eu.peernetwork.app.ui.splash

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun SplashScreen(provider: UiComponentProvider) {
    HomeScaffold()
}

@Composable
private fun HomeScaffold() {
    Text("Splash screen")
}

@Preview
@Composable
fun PreviewSplashScreen() {
    PeerTheme {
        HomeScaffold()
    }
}
