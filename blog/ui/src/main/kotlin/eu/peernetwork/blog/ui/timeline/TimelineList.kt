package eu.peernetwork.blog.ui.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStoreOwner
import androidx.paging.LoadState
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.ui.engagement.EngagementInteractor.Companion.LocalEngagementInteractor
import eu.peernetwork.blog.ui.engagement.EngagementReaction
import eu.peernetwork.blog.ui.engagement.EngagementReaction.Companion.LocalEngagementReaction
import eu.peernetwork.blog.ui.extension.route
import eu.peernetwork.blog.ui.mapper.mapToDetail
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.post.PostItem
import eu.peernetwork.blog.ui.post.PostMedia
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.blog.ui.post.PostNavigator.Companion.LocalPostNavigator
import eu.peernetwork.blog.ui.post.PostSkeleton
import eu.peernetwork.blog.ui.post.PostFollow
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun TimelineList(
    uuid: String,
    username: String,
    imageUrl: String,
    status: State<Boolean>,
    selected: MutableIntState,
    refresh: MutableState<Boolean>,
    limit: Int,
    category: Category,
    criteria: Filter.Criteria,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    listState: LazyListState,
    onExplore: () -> Unit,
    onEvent: (TimelineEvent) -> Unit,
) {
    val enable = remember { derivedStateOf { !status.value } }
    val showSheet = remember { mutableStateOf<UiPost?>(null) }
    val current = rememberSaveable { mutableIntStateOf(-1) }
    TimelineScreen(
        uuid = uuid,
        limit = limit,
        username = username,
        imageUrl = imageUrl,
        category = category,
        criteria = criteria,
        focused = current,
        selected = selected,
        showSheet = showSheet,
        refresh = refresh,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
        listState = listState,
        onEvent = onEvent,
        onExplore = onExplore
    ) { component, items ->
        items(
            count = items.itemCount,
            key = { index -> items[index]?.id?.let { "$it;$index" } ?: index }
        ) { index ->
            items[index]?.let { post ->
                val engagement = LocalEngagementInteractor.current
                val reaction = LocalEngagementReaction.current
                val navigator = LocalPostNavigator.current
                val isAuthor = uuid == post.author.id
                val isVisible = remember { mutableStateOf(post.isAccessible || isAuthor) }
                PostItem(
                    type = post.type,
                    pinnedBy = post.pinnedBy,
                    model = post.mapToDetail(),
                    asset = post.asset,
                    isAuthor = isAuthor,
                    author = post.author,
                    isAccessible = post.isAccessible,
                    isVisible = isVisible,
                    status = post.status,
                    onMenu = { showSheet.value = post },
                    onClick = { selected.intValue = index },
                    onContentClick = { type, value ->
                        navigator.navigate(type.route(value))
                    },
                    onAuthorClick = {
                        navigator.navigate(
                            route = PostNavigator.Route.Profile(post.author.id)
                        )
                    },
                    engagement = {
                        EngagementReaction(
                            post = post,
                            state = engagement.observe()
                        ) { reaction(post, it) }
                    },
                    connection = {
                        if (uuid != post.author.id) {
                            component.postUserFollow()(
                                modifier = Modifier,
                                PostFollow.Spec(
                                    id = post.author.id,
                                    isFollowing = post.author.following,
                                    isFollowed = post.author.followed,
                                )
                            )
                        }
                    },
                    content = { media, expanded ->
                        val isPlaying = remember { derivedStateOf { index == current.intValue } }
                        PostMedia(
                            type = post.type,
                            path = media.path,
                            expanded = expanded,
                            isAdmin = uuid == post.author.id,
                            isAccessible = post.isAccessible,
                            isVisible = isVisible,
                            status = post.status,
                            cover = media.display.cover ?: post.author.imageUrl,
                            ratio = post.asset.ratio,
                            enable = enable,
                            isPlaying = isPlaying,
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
            item(key = category.name) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (items.loadState.append is LoadState.Loading) {
                        PostSkeleton()
                    }
                }
            }
        }
    }
}
