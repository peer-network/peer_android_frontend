package eu.peernetwork.blog.ui.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStoreOwner
import androidx.paging.LoadState
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.engagement.EngagementInteractor.Companion.LocalEngagementInteractor
import eu.peernetwork.blog.ui.engagement.EngagementReaction
import eu.peernetwork.blog.ui.engagement.EngagementReaction.Companion.LocalEngagementReaction
import eu.peernetwork.blog.ui.extension.route
import eu.peernetwork.blog.ui.mapper.mapToDetail
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.post.PostItem
import eu.peernetwork.blog.ui.post.PostMedia
import eu.peernetwork.blog.ui.post.PostNavigator.Companion.LocalPostNavigator
import eu.peernetwork.blog.ui.post.PostSkeleton
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalMaterial3Api::class)
fun ArticleList(
    uuid: String,
    author: String,
    username: String,
    imageUrl: String,
    types: Set<Content.Type>,
    limit: Int,
    status: State<Boolean>,
    selected: MutableIntState,
    timestamp: State<Long>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onEvent: (ArticleEvent) -> Unit,
    listState: LazyListState = rememberLazyListState()
) {
    val enable = remember { derivedStateOf { !status.value } }
    val showSheet = remember { mutableStateOf<UiPost?>(null) }
    val current = rememberSaveable { mutableIntStateOf(-1) }
    ArticleScreen(
        uuid = uuid,
        author = author,
        username = username,
        imageUrl = imageUrl,
        limit = limit,
        types = types,
        focused = current,
        showSheet = showSheet,
        selected = selected,
        timestamp = timestamp,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
        onEvent = onEvent,
        listState = listState
    ) { component, items ->
        items(
            count = items.itemCount,
            key = { index -> items[index]?.id?.let { "$it;$index" } ?: index }
        ) { index ->
            val engagement = LocalEngagementInteractor.current
            val reaction = LocalEngagementReaction.current
            val navigator = LocalPostNavigator.current
            items[index]?.let { post ->
                PostItem(
                    type = post.type,
                    pinnedBy = post.pinnedBy,
                    model = post.mapToDetail(),
                    asset = post.asset,
                    isAuthor = uuid == post.author.id,
                    isAccessible = post.isAccessible,
                    status = post.status,
                    onMenu = { showSheet.value = post },
                    onClick = { selected.intValue = index },
                    onAuthorClick = { selected.intValue = index },
                    engagement = {
                        EngagementReaction(
                            post = post,
                            state = engagement.observe()
                        ) { reaction(post, it) }
                    },
                    connection = { },
                    onContentClick = { type, value ->
                        navigator.navigate(type.route(value))
                    },
                    content = { media, expanded ->
                        val isPlaying = remember { derivedStateOf { index == current.intValue } }
                        PostMedia(
                            type = post.type,
                            path = media.path,
                            expanded = expanded,
                            status = post.status,
                            isAdmin = uuid == post.author.id,
                            isAccessible = post.isAccessible,
                            cover = media.display.cover ?: post.author.imageUrl,
                            ratio = post.asset.ratio,
                            enable = enable,
                            isPlaying = isPlaying
                        ) {
                            if (it) {
                                current.intValue = index
                            } else {
                                current.intValue = -1
                            }
                        }
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
}
