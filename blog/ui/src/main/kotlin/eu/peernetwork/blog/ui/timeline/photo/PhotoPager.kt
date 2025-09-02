package eu.peernetwork.blog.ui.timeline.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.compose.PhotoContent
import eu.peernetwork.blog.ui.compose.TextContent
import eu.peernetwork.blog.ui.event.UiEngagementEvent
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.event.UiModerationEvent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PhotoPager(
    id: String,
    position: Int,
    engagement: UiEngagementEvent,
    moderation: UiModerationEvent,
    lazyPagingItems: State<LazyPagingItems<UiPost>>,
    onAuthorClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    header: @Composable () -> Unit = {},
    indicator: @Composable (PagerState, ImmutableList<UiMedia>) -> Unit = { state, items -> },
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
    content: @Composable (UiPost, PagerState, Boolean) -> Unit = { post, state, active -> },
) {
    val updatedContent by rememberUpdatedState(content)
    val updatedIndicator by rememberUpdatedState(indicator)
    val updatedConnection by rememberUpdatedState(connection)
    val pagerState = rememberPagerState(
        initialPage = position
    ) { lazyPagingItems.value.itemCount }
    VerticalPager(pagerState) { page ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val post = lazyPagingItems.value[page]
            if (post != null) {
                if (post.type == UiPost.Type.TEXT) {
                    TextContent(
                        post = post,
                        onAuthorClick = onAuthorClick,
                        onMentionClick = onMentionClick,
                        onHashtagClick = onHashtagClick,
                        uiEngagementEvent = engagement,
                        uiModerationEvent = moderation,
                        connection = {
                            if (id != post.author.id) {
                                updatedConnection(
                                    Triple(
                                        post.author.id,
                                        post.author.isfollowing,
                                        post.author.isfollowed
                                    )
                                )
                            }
                        },
                        header = header
                    )
                } else if (post.type == UiPost.Type.IMAGE) {
                    val state = rememberPagerState(initialPage = 0) { post.media.size }
                    PhotoContent(
                        post = post,
                        uiEngagementEvent = engagement,
                        uiModerationEvent = moderation,
                        header = header,
                        indicator = { updatedIndicator(state, post.media.toPersistentList()) },
                        onAuthorClick = onAuthorClick,
                        onMentionClick = onMentionClick,
                        onHashtagClick = onHashtagClick,
                        connection = {
                            if (id != post.author.id) {
                                updatedConnection(
                                    Triple(
                                        post.author.id,
                                        post.author.isfollowing,
                                        post.author.isfollowed
                                    )
                                )
                            }
                        },
                    ) { updatedContent(post, state, page == pagerState.currentPage) }
                } else if (post.type == UiPost.Type.AUDIO) {

                } else if (post.type == UiPost.Type.VIDEO) {

                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}
