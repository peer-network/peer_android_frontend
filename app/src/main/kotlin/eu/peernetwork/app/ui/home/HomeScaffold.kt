package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignToolbarTitle
import eu.peernetwork.core.ui.theme.PeerTheme

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
        Box(modifier = Modifier.padding(padding)
        ) { content() }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewHomeScaffold() {
    PeerTheme {
        HomeScaffold(
            header = { HomeHeader(remember { mutableStateOf(DesignToolbarTitle(R.string.home_label)) }) { } },
            footer = { HomeFooter(remember { mutableIntStateOf(0) }) }
        ) {
            Text(
                text = "",
                textAlign = TextAlign.Center
            )
        }
    }
}
