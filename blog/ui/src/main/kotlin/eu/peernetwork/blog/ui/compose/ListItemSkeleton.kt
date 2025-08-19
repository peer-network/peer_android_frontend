package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun ListItemSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize()
            .then(modifier).verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        ListItem()
        ListItem()
    }
}

@Preview
@Composable
fun PreviewSearchItemSkeleton() {
    PeerTheme {
        ListItemSkeleton(Modifier.padding(horizontal = 24.dp))
    }
}
