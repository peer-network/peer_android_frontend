package eu.peernetwork.app.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun HomeScreen() {
    Text("Home screen")
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    PeerTheme {
        HomeScreen()
    }
}
