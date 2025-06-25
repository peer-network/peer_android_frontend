package eu.peernetwork.blog.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.core.ui.theme.LightAccentColor
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun PostPager(
    media: List<UiMedia>,
    content: @Composable (String) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Box(contentAlignment = Alignment.BottomEnd) {
        val pagerState = rememberPagerState(initialPage = 0) { media.size }
        HorizontalPager(state = pagerState) {
            val media = media[it]
            updatedContent(media.path)
        }
        val total = if (media.size <= 3) {
            media.size
        } else {
            3
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentWidth()
                .padding(16.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(2.dp)
        ) {
            repeat(total) {
                val isSelected = when {
                    media.size <= 3 -> pagerState.currentPage == it
                    else -> when (it) {
                        0 -> pagerState.currentPage == 0
                        1 -> pagerState.currentPage in 1 until (media.size - 1)
                        2 -> pagerState.currentPage == (media.size - 1)
                        else -> false
                    }
                }
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) {
                                LightAccentColor
                            } else {
                                MaterialTheme.colorScheme.surfaceTint
                            }
                        )
                )
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewPostPager() {
    PeerTheme {
        PostPager(
            listOf(
                UiMedia("http://localhost", UiMedia.Options("", null)),
                UiMedia("http://localhost", UiMedia.Options("", null)),
                UiMedia("http://localhost", UiMedia.Options("", null)),
                UiMedia("http://localhost", UiMedia.Options("", null)),
                UiMedia("http://localhost", UiMedia.Options("", null)),
            )
        ) {
            Box(modifier = Modifier.aspectRatio(1f))
        }
    }
}