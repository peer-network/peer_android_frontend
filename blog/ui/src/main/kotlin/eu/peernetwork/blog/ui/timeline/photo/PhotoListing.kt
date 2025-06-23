package eu.peernetwork.blog.ui.timeline.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.compose.PostItem
import eu.peernetwork.blog.ui.engagement.EngagementEvent
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationEvent
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.theme.LightAccentColor
import eu.peernetwork.media.core.renderer.ImageView

@Composable
fun PhotoListing(
    id: String,
    component: Photo.Component,
    lazyPagingItems: LazyPagingItems<UiPost>,
    listState: LazyListState,
    engagement: EngagementEvent,
    moderation: ModerationEvent,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onPostClick: (String, Int) -> Unit,
    onAuthorClick: (String) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val icon = painterResource(eu.peernetwork.blog.ui.R.drawable.ic_gallery)
    val viewMore = stringResource(eu.peernetwork.blog.ui.R.string.view_more_label)
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = { index -> lazyPagingItems[index]?.id?.let { "$it;$index" } ?: index }
        ) { index ->
            lazyPagingItems[index]?.let { post ->
                PhotoListing(
                    id = id,
                    post = post,
                    index = index,
                    engagementEvent = engagement,
                    moderationEvent = moderation,
                    onAuthorClick = onAuthorClick,
                    onPostClick = onPostClick,
                    onHashtagClick = onHashtagClick,
                    onMentionClick = onMentionClick,
                    connection = connection,
                    content = {
                        if (post.media.size > 1) {
                            Box(contentAlignment = Alignment.BottomEnd) {
                                val pagerState = rememberPagerState(initialPage = 0) { post.media.size }
                                HorizontalPager(state = pagerState) {
                                    val media = post.media[it]
                                    component.imageView()(
                                        Modifier,
                                        ImageView.Spec(media.path, post.aspectRatio)
                                    )
                                }
                                Icon(
                                    painter = icon,
                                    contentDescription = viewMore,
                                    tint = LightAccentColor,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .size(16.dp)
                                )
                            }
                        } else {
                            val media = post.media.first()
                            component.imageView()(
                                Modifier,
                                ImageView.Spec(media.path, post.aspectRatio)
                            )
                        }
                    }
                )
            }
        }
        item(key = id) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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
fun LazyItemScope.PhotoListing(
    id: String,
    post: UiPost,
    index: Int,
    engagementEvent: EngagementEvent,
    moderationEvent: ModerationEvent,
    onAuthorClick: (String) -> Unit = {},
    onPostClick: (String, Int) -> Unit,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit,
    content: @Composable (UiPost) -> Unit = {}
) {
    val uiContent = post.mapToContent()
    val clickHandler by rememberUpdatedState { onAuthorClick(post.author.id) }
    val updatedConnection by rememberUpdatedState(connection)
    val handleOnPostClick by rememberUpdatedState(onPostClick)
    PostItem(
        post = post,
        position = index,
        onClick = { handleOnPostClick(post.id, index) },
        onAuthorClick = clickHandler,
        onMentionClick = onMentionClick,
        onHashtagClick = onHashtagClick,
        engagements = {
            EngagementScreen(
                uiContent,
                engagementEvent,
            ) },
        moderation = {
            ModerationScreen(
                uiContent,
                moderationEvent
            )
        },
        content = content,
        actions = {
            if (id != post.author.id) {
                updatedConnection(
                    Triple(
                        post.author.id,
                        post.author.isfollowing,
                        post.author.isfollowed
                    )
                )
            }
        }
    )
}
