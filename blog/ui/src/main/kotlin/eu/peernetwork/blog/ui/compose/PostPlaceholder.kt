package eu.peernetwork.blog.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun PostPlaceholder(
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues
) {
    PostScaffold(
        modifier = modifier.padding(contentPaddingValues),
        header = {
            Row {
                DesignAvatar {
                    Box(
                        modifier = Modifier.size(36.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
            }
        },
        toolbar = {},
        background = {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        RoundedCornerShape(24.dp)
                    )
            ) },
        footer = {}
    ) { Box(modifier = Modifier.fillMaxWidth().height(120.dp)) }
}

@Composable
fun PostPlaceholder(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) { PostPlaceholder(modifier, PaddingValues(16.dp)) }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewPostPlaceholder() {
    PeerTheme {
        PostPlaceholder(modifier = Modifier.padding(8.dp))
    }
}
