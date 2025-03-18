package eu.peernetwork.app.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@Composable
fun HomeScaffold(
    header: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = { header() },
        bottomBar = { footer() }
    ) { padding ->
        content(padding)
    }
}
