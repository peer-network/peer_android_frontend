package eu.peernetwork.blog.ui.article

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import eu.peernetwork.blog.ui.compose.PostPlaceholder
import eu.peernetwork.blog.ui.engagement.v2.EngagementOption
import eu.peernetwork.blog.ui.mapper.mapToDetail
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.v2.ModerationScreenEvent
import eu.peernetwork.blog.ui.post.PostMedia
import eu.peernetwork.blog.ui.post.PostScreen
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignLoader
import kotlinx.coroutines.flow.Flow

@Composable
fun ArticleListing(
    id: String,
    limit: Int,
    state: State<DesignStreamState<Flow<PagingData<UiPost>>>>,
    status: State<Boolean>,
    listState: LazyListState,
    component: Article.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit,
    onMenu: (UiPost) -> Unit,
    content: @Composable (ModerationScreenEvent) -> Unit
) {
    val enable = remember { derivedStateOf { !listState.isScrollInProgress } }
    PostScreen(
        id = id,
        limit = limit,
        listState = listState,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner,
        connection = connection
    ) { postComponent, event, moderation, position ->
        ArticleListing(
            author = id,
            state = state,
            listState = listState,
            onMenu = onMenu,
            connection = connection,
            engagement = { post ->
                EngagementOption(
                    post = post,
                    state = event.observe()
                ) { event(post, it) }
            }
        ) { post, path, index ->
            val isActive = remember { derivedStateOf { index == position.value } }
            PostMedia(
                type = post.type,
                path = path,
                avatar = post.author.imageUrl,
                position = index,
                aspectRatio = post.aspectRatio,
                status = status,
                enable = enable,
                isActive = isActive,
                component = postComponent,
                viewModelStoreOwner = viewModelStoreOwner,
            )
        }
        content(moderation)
    }
}

@Composable
fun ArticleListing(
    author: String,
    state: State<DesignStreamState<Flow<PagingData<UiPost>>>>,
    listState: LazyListState,
    onMenu: (UiPost) -> Unit,
    engagement: @Composable (UiPost) -> Unit,
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit,
    content: @Composable (UiPost, String, Int) -> Unit,
) {
    val updatedEngagement by rememberUpdatedState(engagement)
    val updatedConnection by rememberUpdatedState(connection)
    val updatedContent by rememberUpdatedState(content)
    val handleMenu by rememberUpdatedState(onMenu)
    DesignStream(state) { result ->
        val lazyPagingItems = result.value.collectAsLazyPagingItems()
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = lazyPagingItems.itemCount,
                key = { index -> lazyPagingItems[index]?.id?.let { "$it;$index" } ?: index }
            ) { index ->
                lazyPagingItems[index]?.let { post ->
                    PostScreen(
                        type = post.type,
                        pinnedBy = null,
                        model = post.mapToDetail(),
                        media = post.media,
                        onMenu = { handleMenu(post) },
                        engagement = { updatedEngagement(post) },
                        connection = {
                            updatedConnection(
                                Triple(
                                    post.author.id,
                                    post.author.isfollowing,
                                    post.author.isfollowed
                                )
                            )
                        },
                        content = { path -> updatedContent(post, path, index) }
                    )
                }
            }
            if (lazyPagingItems.loadState.refresh !is LoadState.Loading) {
                item(key = author) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (lazyPagingItems.loadState.append is LoadState.Loading) {
                            DesignLoader {
                                PostPlaceholder(
                                    contentPaddingValues = PaddingValues(16.dp)
                                )
                            }
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp))
                    }
                }
            }
        }
    }
}
