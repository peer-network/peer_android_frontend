package eu.peernetwork.blog.ui.post.photo

import androidx.compose.foundation.layout.Box
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
    author: String,
    component: Photo.Component,
    lazyPagingItems: LazyPagingItems<UiPost>,
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
            lazyPagingItems[index]?.let { photo ->
                PhotoListing(
                    post = photo,
                    index = index,
                    onPostClick = onPostClick,
                    onMentionClick = onMentionClick,
                    onHashtagClick = onHashtagClick,
                    engagementEvent = engagement,
                    moderationEvent = moderation
                ) {
                    if (photo.media.size > 1) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            val pagerState = rememberPagerState(initialPage = 0) { photo.media.size }
                            HorizontalPager(state = pagerState) {
                                val media = photo.media[it]
                                component.imageView()(
                                    Modifier,
                                    ImageView.Spec(media.path, photo.aspectRatio)
                                )
                            }
                            Icon(
                                painter = painterResource(eu.peernetwork.blog.ui.R.drawable.ic_gallery),
                                contentDescription = stringResource(eu.peernetwork.blog.ui.R.string.post_description),
                                tint = LightAccentColor,
                                modifier = Modifier.padding(16.dp)
                                    .size(16.dp)
                            )
                        }
                    } else {
                        val media = photo.media.first()
                        component.imageView()(
                            Modifier,
                            ImageView.Spec(media.path, photo.aspectRatio)
                        )
                    }
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
fun LazyItemScope.PhotoListing(
    post: UiPost,
    index: Int,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onPostClick: (String, Int) -> Unit,
    engagementEvent: EngagementEvent,
    moderationEvent: ModerationEvent,
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
            engagementEvent
        ) },
        moderation = {
            ModerationScreen(
                model = uiContent,
                event = moderationEvent
            )
        },
        content = content
    )
}
