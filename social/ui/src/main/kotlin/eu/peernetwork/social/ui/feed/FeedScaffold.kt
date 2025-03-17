package eu.peernetwork.social.ui.feed

import android.content.res.Configuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun FeedScaffold(
    content: @Composable () -> Unit,
) {
    content()
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewFeedScaffold() {
    PeerTheme {
        FeedScaffold {
            Text("Photo")
        }
    }
}
