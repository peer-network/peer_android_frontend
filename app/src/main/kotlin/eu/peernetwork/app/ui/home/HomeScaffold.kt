package eu.peernetwork.app.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScaffold(
    header: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = { header() },
        bottomBar = { footer() }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) { content() }
    }
}
