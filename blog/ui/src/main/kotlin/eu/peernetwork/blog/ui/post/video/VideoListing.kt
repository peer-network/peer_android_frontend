package eu.peernetwork.blog.ui.post.video

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.compose.MediaView
import eu.peernetwork.blog.ui.compose.TextView
import eu.peernetwork.blog.ui.engagement.EngagementEvent
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.moderation.ModerationEvent
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.media.core.renderer.VideoThumbnail

@Composable
fun VideoListing(
    author: String,
    component: Video.Component,
    lazyPagingItems: LazyPagingItems<UiVideo>,
    listState: LazyListState,
    engagement: EngagementEvent,
    moderation: ModerationEvent,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onPostClick: (String, Int) -> Unit,
) {
    LazyColumn(state = listState) {
        items(
            count = lazyPagingItems.itemCount,
            key = { index -> lazyPagingItems[index]?.id?.let { "$it;$index" } ?: index }
        ) { index ->
            lazyPagingItems[index]?.let { post ->
                VideoListing(
                    post = post,
                    index = index,
                    onMentionClick = onMentionClick,
                    onHashtagClick = onHashtagClick,
                    engagementEvent = engagement,
                    moderationEvent = moderation,
                    onPostClick = onPostClick,
                ) {
                    component.videoThumbnail()(
                        Modifier,
                        VideoThumbnail.Spec(post.media, post.aspectRatio, post.resolution)
                    )
                }
            }
        }
        item(key = author) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                if (lazyPagingItems.loadState.append is LoadState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun VideoListing(
    post: UiVideo,
    index: Int,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    engagementEvent: EngagementEvent,
    moderationEvent: ModerationEvent,
    onPostClick: (String, Int) -> Unit,
    content: @Composable (UiVideo) -> Unit = {}
) {
    val updatedContent by rememberUpdatedState(content)
    val selectionHandler by rememberUpdatedState { onPostClick(post.id, index) }
    val uiContent = post.mapToContent()
    MediaView(
        author = post.author,
        description = post.time,
        modifier = Modifier.padding(bottom = 16.dp),
        caption = {
            TextView(
                post.author.username,
                post.title,
                post.description,
                onMentionClick = onMentionClick,
                onHashtagClick = onHashtagClick
            )
        },
        engagements = {
            EngagementScreen(
                uiContent,
                engagementEvent
            )
        },
        moderation = {
            ModerationScreen(
                uiContent,
                moderationEvent
            )
        },
    ) {
        Box(modifier = Modifier.clickable(
            role = Role.Button,
            onClick = selectionHandler
        )) { updatedContent(post) }
    }
}
