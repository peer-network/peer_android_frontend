package eu.peernetwork.blog.ui.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
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
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.ui.engagement.EngagementInteractor.Companion.LocalEngagementInteractor
import eu.peernetwork.blog.ui.engagement.EngagementReaction
import eu.peernetwork.blog.ui.engagement.EngagementReaction.Companion.LocalEngagementReaction
import eu.peernetwork.blog.ui.mapper.v2.mapToDetail
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.blog.ui.post.PostItem
import eu.peernetwork.blog.ui.post.PostMedia
import eu.peernetwork.blog.ui.post.PostSkeleton
import eu.peernetwork.blog.ui.post.PostUserConnection
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun TimelineList(
    id: String,
    username: String,
    imageUrl: String,
    status: State<Boolean>,
    selected: MutableIntState,
    limit: Int,
    category: Category,
    criteria: Filter.Criteria,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    listState: LazyListState,
    onEvent: (TimelineEvent) -> Unit,
    onExplore: (() -> Unit)? = null,
) {
    val enable = remember { derivedStateOf { !status.value } }
    val showSheet = remember { mutableStateOf<UiPost?>(null) }
    val current = rememberSaveable { mutableIntStateOf(-1) }
    TimelineScreen(
        limit = limit,
        username = username,
        imageUrl = imageUrl,
        category = category,
        criteria = criteria,
        focused = current,
        selected = selected,
        showSheet = showSheet,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
        listState = listState,
        onEvent = onEvent,
    ) { component, items ->
        items(
            count = items.itemCount,
            key = { index -> items[index]?.id?.let { "$it;$index" } ?: index }
        ) { index ->
            items[index]?.let { post ->
                val engagement = LocalEngagementInteractor.current
                val reaction = LocalEngagementReaction.current
                PostItem(
                    type = post.type,
                    pinnedBy = post.pinnedBy,
                    model = post.mapToDetail(),
                    asset = post.asset,
                    onMenu = { showSheet.value = post },
                    onClick = { selected.intValue = index },
                    engagement = {
                        EngagementReaction(
                            post = post,
                            state = engagement.observe()
                        ) { reaction(post, it) }
                    },
                    connection = {
                        if (id != post.author.id) {
                            component.postUserFollow()(
                                modifier = Modifier,
                                PostUserConnection.Spec(
                                    id = post.author.id,
                                    isFollowing = post.author.following,
                                    isFollowed = post.author.followed
                                )
                            )
                        }
                    },
                    content = { path ->
                        val isActive = remember { derivedStateOf { index == current.intValue } }
                        PostMedia(
                            type = post.type,
                            path = path,
                            avatar = post.author.imageUrl,
                            position = index,
                            aspectRatio = post.asset.ratio,
                            enable = enable,
                            isPlaying = isActive,
                        )
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
