package eu.peernetwork.app.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun ProcessCycle(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Box(modifier = modifier) {
        updatedContent()
        Box(modifier = Modifier.padding(56.dp)
            .fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.ic_top_left),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(42.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_bottom_left),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(42.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_bottom_right),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(42.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_top_right),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(42.dp)
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun ProcessCyclePreview() {
    PeerTheme {
        Box(Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center) {
            ProcessCycle(modifier = Modifier.fillMaxSize(fraction = .8f)
                .aspectRatio(1f)) {}
        }
    }
}
