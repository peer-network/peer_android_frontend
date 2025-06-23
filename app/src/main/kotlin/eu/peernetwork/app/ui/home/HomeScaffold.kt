package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import eu.peernetwork.core.ui.design.compose.DesignScaffold
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun HomeScaffold(
    snackbarHostState: SnackbarHostState,
    header: @Composable (State<Float>) -> Unit,
    footer: @Composable (State<Float>) -> Unit,
    content: @Composable (State<Float>) -> Unit,
) {
    DesignScaffold(
        snackbarHostState = snackbarHostState,
        alwaysReturn = true,
        header = header,
        footer = footer,
        modifier = Modifier.statusBarsPadding()
            .navigationBarsPadding()
    ) { state -> content(state) }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewHomeScaffold() {
    PeerTheme {
        HomeScaffold(
            snackbarHostState = SnackbarHostState(),
            header = {  },
            footer = { HomeFooter(remember { mutableIntStateOf(0) }) { prev, next -> } }
        ) { state ->
            Text(
                text = "",
                textAlign = TextAlign.Center
            )
        }
    }
}
