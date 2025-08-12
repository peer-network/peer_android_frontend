package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.event.UiEngagementEvent
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.event.UiModerationEvent

@Composable
fun VideoPage(
    position: Int,
    enabled: Boolean,
    engagement: UiEngagementEvent,
    moderation: UiModerationEvent,
    lazyPagingItems: LazyPagingItems<UiVideo>,
    onPostClick: (String, Int) -> Unit,
    onAuthorClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    progress: @Composable (MutableFloatState) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
    header: @Composable () -> Unit = {},
    background: @Composable (UiVideo) -> Unit = {},
    content: @Composable (UiVideo, Boolean, MutableFloatState) -> Unit = { post, shouldPlay, progress -> },
) {
    val updatedContent by rememberUpdatedState(content)
    val updatedProgress by rememberUpdatedState(progress)
    val updatedBackground by rememberUpdatedState(background)
    val pagerState = rememberPagerState(
        initialPage = position
    ) { lazyPagingItems.itemCount }
    VerticalPager(pagerState) { page ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val post = lazyPagingItems[page]
            val progress = remember { mutableFloatStateOf(0f) }
            if (post != null) {
                VideoContent(
                    post = post,
                    index = page,
                    uiEngagementEvent = engagement,
                    uiModerationEvent = moderation,
                    onAuthorClick = onAuthorClick,
                    onPostClick = onPostClick,
                    onMentionClick = onMentionClick,
                    onHashtagClick = onHashtagClick,
                    header = header,
                    connection = connection,
                    progress = { updatedProgress(progress) },
                    background = { updatedBackground(post) }
                ) { updatedContent(post, enabled && page == pagerState.currentPage, progress) }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}
