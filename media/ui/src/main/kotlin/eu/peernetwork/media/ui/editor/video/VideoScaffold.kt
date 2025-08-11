package eu.peernetwork.media.ui.editor.video

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun VideoScaffold(
    footer: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val updatedContent by rememberUpdatedState(content)
    val updatedFooter by rememberUpdatedState(footer)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.weight(1f)) {
            updatedContent()
        }
        updatedFooter()
        Spacer(modifier = Modifier.weight(.3f))
    }
}

@Composable
fun VideoScaffold() {
    val size = 5
    VideoScaffold(
        footer = {
            Row {
                Spacer(modifier = Modifier.width(24.dp))
                repeat(size) {
                    Box(modifier = Modifier.weight(1f)
                        .aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.surfaceVariant))
                    if (it != size - 1) {
                        Spacer(modifier = Modifier.width(3.dp))
                    }
                }
            }
        }
    ) {
        Box(modifier = Modifier
            .padding(bottom = 24.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant))
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewVideoScaffold() {
    PeerTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            VideoScaffold()
        }
    }
}