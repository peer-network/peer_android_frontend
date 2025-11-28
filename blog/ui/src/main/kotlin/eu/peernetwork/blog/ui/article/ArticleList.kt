package eu.peernetwork.blog.ui.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.LoadState
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.engagement.EngagementOption
import eu.peernetwork.blog.ui.mapper.v2.mapToDetail
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.blog.ui.post.PostItem
import eu.peernetwork.blog.ui.post.PostMedia
import eu.peernetwork.blog.ui.post.PostSkeleton
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalMaterial3Api::class)
fun ArticleList(
    author: String,
    types: Set<Content.Type>,
    limit: Int,
    status: State<Boolean>,
    selected: MutableIntState,
    timestamp: State<Long>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onEvent: (ArticleEvent) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val enable = remember { derivedStateOf { !listState.isScrollInProgress } }
    val pause = remember { mutableStateOf(false) }
    val showSheet = remember { mutableStateOf<UiPost?>(null) }
    val current = rememberSaveable { mutableIntStateOf(-1) }
    val updatedConnection by rememberUpdatedState(connection)
    val lifecycleObserver = remember {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    pause.value = false
                }
                Lifecycle.Event.ON_STOP -> {
                    pause.value = true
                }
                else -> Unit
            }
        }
    }
    ArticleScreen(
        id = author,
        limit = limit,
        types = types,
        focused = current,
        showSheet = showSheet,
        selected = selected,
        timestamp = timestamp,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
        onEvent = onEvent,
        listState = listState,
        connection = connection
    ) { component, handle, items ->
        items(
            count = items.itemCount,
            key = { index -> items[index]?.id?.let { "$it;$index" } ?: index }
        ) { index ->
            items[index]?.let { post ->
                PostItem(
                    type = post.type,
                    pinnedBy = null,
                    model = post.mapToDetail(),
                    asset = post.asset,
                    onMenu = { showSheet.value = post },
                    onClick = { selected.intValue = index },
                    engagement = {
                        EngagementOption(
                            post = post,
                            state = handle.engagementOption().observe()
                        ) { handle.engagementOption()(post, it) }
                    },
                    connection = {
                        updatedConnection(
                            Triple(
                                post.author.id,
                                post.author.following,
                                post.author.followed
                            )
                        )
                    },
                    content = { path ->
                        val isActive = remember { derivedStateOf { index == current.intValue } }
                        PostMedia(
                            type = post.type,
                            path = path,
                            avatar = post.author.imageUrl,
                            position = index,
                            aspectRatio = post.asset.ratio,
                            status = status,
                            enable = enable,
                            isActive = isActive,
                            component = handle.component(),
                            viewModelStoreOwner = viewModelStoreOwner,
                        )
                    }
                )
            }
        }
        if (items.loadState.refresh !is LoadState.Loading) {
            item(key = author) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (items.loadState.append is LoadState.Loading) {
                        PostSkeleton()
                    }
                }
            }
        }
    }
    DisposableEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
}
