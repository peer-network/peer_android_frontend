package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.engagement.Engagements
import eu.peernetwork.blog.ui.mapper.query
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.moderation.ModerationAction
import eu.peernetwork.media.core.model.UiMimeType

@Composable
fun VideoPage(
    position: Int,
    enabled: Boolean,
    engagement: Engagements,
    moderation: ModerationAction,
    lazyPagingItems: LazyPagingItems<UiVideo>,
    onLoad: (UiVideo) -> Unit,
    onPostClick: (String, Int) -> Unit,
    onAuthorClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    progress: @Composable (MutableFloatState) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
    header: @Composable () -> Unit = {},
    background: @Composable (String) -> Unit = {},
    content: @Composable (UiVideo, Boolean, MutableFloatState) -> Unit = { post, shouldPlay, progress -> },
) {
    val updatedContent by rememberUpdatedState(content)
    val updatedProgress by rememberUpdatedState(progress)
    val updatedBackground by rememberUpdatedState(background)
    val handleLoad by rememberUpdatedState(onLoad)
    val pagerState = rememberPagerState(
        initialPage = position
    ) { lazyPagingItems.itemCount }
    val canLoad = remember { derivedStateOf {
        pagerState.layoutInfo.pageSize > 0 && !pagerState.isScrollInProgress
    } }
    VerticalPager(pagerState) { page ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val post = lazyPagingItems[page]
            var progress = remember { mutableFloatStateOf(0f) }
            if (post != null) {
                VideoContent(
                    post = post,
                    index = page,
                    engagements = engagement,
                    moderationAction = moderation,
                    onAuthorClick = onAuthorClick,
                    onPostClick = onPostClick,
                    onMentionClick = onMentionClick,
                    onHashtagClick = onHashtagClick,
                    header = header,
                    connection = connection,
                    progress = { updatedProgress(progress) },
                    background = { updatedBackground("${post.media}${UiMimeType.Video.query()}") }
                ) { updatedContent(post, enabled && page == pagerState.currentPage, progress) }
            } else {
                CircularProgressIndicator()
            }
            LaunchedEffect(canLoad.value) {
                if (canLoad.value) {
                    post?.let { handleLoad(it) }
                }
            }
        }
    }
}
