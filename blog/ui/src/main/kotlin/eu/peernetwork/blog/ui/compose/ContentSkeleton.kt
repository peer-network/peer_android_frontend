package eu.peernetwork.blog.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignAvatar
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun ContentSkeleton(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = modifier) {
            repeat(3) {
                DesignAvatar(modifier = Modifier.padding(top = 16.dp)) {
                    Box(
                        modifier = Modifier.size(36.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewContentSkeleton() {
    PeerTheme {
        ContentSkeleton(modifier = Modifier.padding(8.dp))
    }
}
