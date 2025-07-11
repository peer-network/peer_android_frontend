package eu.peernetwork.blog.ui.timeline.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.compose.PhotoIndicator
import eu.peernetwork.blog.ui.compose.PostItem
import eu.peernetwork.blog.ui.compose.PhotoPager
import eu.peernetwork.blog.ui.engagement.Engagements
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.Moderations
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.media.core.renderer.ImageView

@Composable
fun PhotoListing(
    id: String,
    component: Photo.Component,
    lazyPagingItems: LazyPagingItems<UiPost>,
    listState: LazyListState,
    engagement: Engagements,
    moderation: Moderations,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onPostClick: (String, Int) -> Unit,
    onAuthorClick: (String) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
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
                    engagements = engagement,
                    moderations = moderation,
                    onAuthorClick = onAuthorClick,
                    onPostClick = onPostClick,
                    onHashtagClick = onHashtagClick,
                    onMentionClick = onMentionClick,
                    connection = connection,
                    content = {
                        if (post.media.size > 1) {
                            val pagerState = rememberPagerState(initialPage = 0) { post.media.size }
                            PhotoPager(
                                pagerState,
                                post.aspectRatio,
                                post.media,
                                { PhotoIndicator(pagerState, post.media) }
                            ) { path ->
                                component.imageView()(
                                    Modifier,
                                    ImageView.Spec(
                                        path,
                                        null,
                                        ContentScale.Crop,
                                        500f,
                                    )
                                )
                                component.imageView()(
                                    Modifier,
                                    ImageView.Spec(path, post.aspectRatio)
                                )
                            }
                        } else {
                            val media = post.media.first()
                            component.imageView()(
                                Modifier,
                                ImageView.Spec(
                                    media.path,
                                    post.aspectRatio,
                                    ContentScale.Crop,
                                    500f,
                                )
                            )
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
    engagements: Engagements,
    moderations: Moderations,
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
                engagements,
            ) },
        moderation = {
            ModerationScreen(
                uiContent,
                moderations
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
