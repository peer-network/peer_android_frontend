package eu.peernetwork.blog.ui.post.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.compose.ListView
import eu.peernetwork.blog.ui.compose.PostItem
import eu.peernetwork.blog.ui.compose.PostContent
import eu.peernetwork.blog.ui.event.UiEngagementEvent
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.event.UiModerationEvent
import eu.peernetwork.blog.ui.moderation.ModerationScreen

@Composable
fun PhotoListing(
    author: String,
    component: Photo.Component,
    lazyPagingItems: LazyPagingItems<UiPost>,
    listState: LazyListState,
    viewModel: PhotoViewModel,
    engagement: UiEngagementEvent,
    moderation: UiModerationEvent,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onPostClick: (String, Int) -> Unit,
) {
    val current = remember { mutableIntStateOf(-1) }
    val imageView = remember { component.imageView() }
    val audioPlayer = remember { component.audioPlayer() }
    ListView(
        listState = listState,
        onFocused = { current.intValue = it },
        onFocus = { position ->
            if (position < lazyPagingItems.itemCount) {
                lazyPagingItems[position]?.let {
                    if (!it.isViewed) {
                        viewModel.view(it.id)
                    }
                }
            }
        }
    ) { position ->
        LazyColumn(state = listState) {
            items(
                count = lazyPagingItems.itemCount,
                key = { index -> lazyPagingItems[index]?.id?.let { "$it;$index" } ?: index }
            ) { index ->
                lazyPagingItems[index]?.let { photo ->
                    val enable = remember(position.value) { derivedStateOf { index == position.value } }
                    PhotoListing(
                        post = photo,
                        index = index,
                        onPostClick = onPostClick,
                        onMentionClick = onMentionClick,
                        onHashtagClick = onHashtagClick,
                        uiEngagementEvent = engagement,
                        uiModerationEvent = moderation
                    ) {
                        PostContent(
                            post = photo,
                            position = index,
                            imageView = imageView,
                            audioPlayer = audioPlayer,
                            enable = enable,
                            current = current
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
}

@Composable
fun PhotoListing(
    post: UiPost,
    index: Int,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onPostClick: (String, Int) -> Unit,
    uiEngagementEvent: UiEngagementEvent,
    uiModerationEvent: UiModerationEvent,
    content: @Composable (UiPost) -> Unit = {}
) {
    val uiContent = post.mapToContent()
    val handleOnPostClick by rememberUpdatedState(onPostClick)
    PostItem(
        post,
        index,
        onClick = { handleOnPostClick(post.id, index) },
        onMentionClick = onMentionClick,
        onHashtagClick = onHashtagClick,
        engagements = { EngagementScreen(
            uiContent,
            uiEngagementEvent
        ) },
        moderation = {
            ModerationScreen(
                model = uiContent,
                event = uiModerationEvent
            )
        },
        audio = content,
        video = content,
        image = content,
    )
}
