package eu.peernetwork.blog.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun PhotoPager(
    state: PagerState,
    ratio: Float,
    media: List<UiMedia>,
    indicator: @Composable () -> Unit = {},
    content: @Composable (String) -> Unit
) {
    val updatedIndicator by rememberUpdatedState(indicator)
    val updatedContent by rememberUpdatedState(content)
    Box(contentAlignment = Alignment.BottomEnd) {
        HorizontalPager(state = state) {
            val media = media[it]
            Box(modifier = Modifier.then(
                if (ratio == 0f) {
                    Modifier
                } else {
                    Modifier.fillMaxWidth()
                        .aspectRatio(ratio)
                }
            ),
                contentAlignment = Alignment.Center) {
                updatedContent(media.path)
            }
        }
        Box(modifier = Modifier.padding(16.dp)) {
            updatedIndicator()
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewPostPager() {
    PeerTheme {
        val items = listOf(
            UiMedia("http://localhost", UiMedia.Options("", null)),
            UiMedia("http://localhost", UiMedia.Options("", null)),
            UiMedia("http://localhost", UiMedia.Options("", null)),
            UiMedia("http://localhost", UiMedia.Options("", null)),
            UiMedia("http://localhost", UiMedia.Options("", null)),
        )
        val state = rememberPagerState(initialPage = 0) { items.size }
        PhotoPager(
            state,
            1f,
            items,
            { Text("Hello") }
        ) {
            Box(modifier = Modifier.aspectRatio(1f))
        }
    }
}
