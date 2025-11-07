package eu.peernetwork.blog.ui.post

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
import androidx.compose.foundation.pager.PagerState
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
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PostPager(
    state: PagerState,
    media: ImmutableList<UiMedia>,
    content: @Composable (String) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Box(contentAlignment = Alignment.BottomEnd) {
        HorizontalPager(state = state) {
            content(media[it].path)
        }
        Box(modifier = Modifier.padding(horizontal = 24.dp)
            .padding(vertical = 16.dp)) {
            PostPagerIndicator(
                state,
                media
            )
        }
    }
}

@Composable
fun PostPagerIndicator(
    state: PagerState,
    media: ImmutableList<UiMedia>,
    modifier: Modifier = Modifier
) {
    val total = if (media.size <= 3) {
        media.size
    } else {
        3
    }
    if (total > 1) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentWidth()
                .then(modifier)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(2.dp)
        ) {
            repeat(total) {
                val isSelected = when {
                    media.size <= 3 -> state.currentPage == it
                    else -> when (it) {
                        0 -> state.currentPage == 0
                        1 -> state.currentPage in 1 until (media.size - 1)
                        2 -> state.currentPage == (media.size - 1)
                        else -> false
                    }
                }
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) {
                                MaterialTheme.colorScheme.background
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
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
        val items = persistentListOf(
            UiMedia("http://localhost", UiMedia.Options("", null)),
            UiMedia("http://localhost", UiMedia.Options("", null)),
            UiMedia("http://localhost", UiMedia.Options("", null)),
            UiMedia("http://localhost", UiMedia.Options("", null)),
            UiMedia("http://localhost", UiMedia.Options("", null)),
        )
        val state = rememberPagerState(initialPage = 0) { items.size }
        PostPager(
            state,
            items,
        ) {
            Box(modifier = Modifier.aspectRatio(1f))
        }
    }
}
