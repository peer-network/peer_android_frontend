package eu.peernetwork.app.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import eu.peernetwork.core.ui.theme.PeerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeHeader() {
    TopAppBar(
        title = { Text("Home") },
        navigationIcon = {},
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More")
            }
        }
    )
}

@Preview
@Composable
fun PreviewHomeHeader() {
    PeerTheme {
        HomeHeader()
    }
}
