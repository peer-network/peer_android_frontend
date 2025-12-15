package eu.peernetwork.blog.ui.timeline

import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.extension.share
import eu.peernetwork.blog.ui.gallery.GalleryScreen
import eu.peernetwork.blog.ui.mapper.mapToDetail
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationInteractor.Companion.LocalModerationInteractor
import eu.peernetwork.blog.ui.post.PostScreen
import eu.peernetwork.blog.ui.post.PostUserConnection
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun TimelineModal(
    uuid: String,
    username: String,
    imageUrl: String,
    selected: MutableIntState,
    limit: Int,
    category: Category,
    criteria: Criteria,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onEvent: (TimelineEvent) -> Unit,
) {
    val showSheet = remember { mutableStateOf<UiPost?>(null) }
    TimelineModal(
        uuid = uuid,
        username = username,
        imageUrl = imageUrl,
        limit = limit,
        selected = selected,
        category = category,
        criteria = criteria,
        showSheet = showSheet,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
        onEvent = onEvent,
    ) { component, post, index, pagerState ->
        GalleryScreen(
            position = index,
            selected = selected,
            enabled = !pagerState.isScrollInProgress,
            post = post,
            showSheet = showSheet
        ) {
            if (uuid != post.author.id) {
                component.postUserFollow()(
                    modifier = Modifier,
                    PostUserConnection.Spec(
                        id = post.author.id,
                        isFollowing = post.author.following,
                        isFollowed = post.author.followed
                    )
                )
            }
        }
    }
}

@Composable
fun TimelineModal(
    uuid: String,
    username: String,
    imageUrl: String,
    selected: MutableIntState,
    showSheet: MutableState<UiPost?>,
    limit: Int,
    category: Category,
    criteria: Criteria,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onEvent: (TimelineEvent) -> Unit,
    content: @Composable PagerScope.(Timeline.Component, UiPost, Int, PagerState) -> Unit
) {
    val context = LocalContext.current
    val handleEvent by rememberUpdatedState(onEvent)
    val updatedContent by rememberUpdatedState(content)
    val shareTitle = stringResource(R.string.share_label)
    TimelineScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        TimelineScreen(
            limit = limit,
            category = category,
            criteria = criteria,
            component = component,
            viewModel = viewModel,
            loading = {},
            onExplore = {}
        ) { component, items ->
            val pagerState = rememberPagerState(initialPage = selected.intValue) {
                items.itemCount
            }
            PostScreen(
                uuid = uuid,
                imageUrl = imageUrl,
                username = username,
                limit = limit,
                pagerState = pagerState,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                onComment = { items.itemSnapshotList.getOrNull(it)?.mapToDetail() },
                modal = {
                    val moderation = LocalModerationInteractor.current
                    TimelineSheet(
                        uuid = uuid,
                        state = showSheet
                    ) { sheetState, post ->
                        when (sheetState) {
                            TimelineSheetMenuItem.REPORT -> moderation.onReport(post.id)
                            TimelineSheetMenuItem.SHARE -> { context.share(post.url, shareTitle) }
                            TimelineSheetMenuItem.BOOST -> { handleEvent(TimelineEvent.Boost(post.id)) }
                        }
                    }
                }
            ) { index ->
                items[index]?.let {
                    updatedContent(this, component, it, index, pagerState)
                }
                LaunchedEffect(Unit) {
                    items.itemSnapshotList.getOrNull(pagerState.currentPage)?.let { post ->
                        viewModel.view(post.id)
                    }
                }
            }
        }
    }
}
