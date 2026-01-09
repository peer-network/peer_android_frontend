package eu.peernetwork.blog.ui.detail

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.extension.share
import eu.peernetwork.blog.ui.gallery.GalleryScreen
import eu.peernetwork.blog.ui.mapper.mapToDetail
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationInteractor.Companion.LocalModerationInteractor
import eu.peernetwork.blog.ui.post.PostScreen
import eu.peernetwork.blog.ui.post.PostFollow
import eu.peernetwork.blog.ui.timeline.TimelineSheet
import eu.peernetwork.blog.ui.timeline.TimelineSheetMenuItem
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun DetailModal(
    uuid: String,
    postId: String,
    username: String,
    imageUrl: String,
    selected: MutableIntState,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onBoost: (String) -> Unit,
) {
    val context = LocalContext.current
    val showSheet = remember { mutableStateOf<UiPost?>(null) }
    val shareTitle = stringResource(R.string.share_label)
    val handleBoost by rememberUpdatedState(onBoost)
    DetailScreen(
        uuid = uuid,
        username = username,
        imageUrl = imageUrl,
        limit = limit,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
    ) { component, viewModel ->
        DetailScreen(
            id = postId,
            component = component,
            viewModel = viewModel,
        ) { postState ->
            val pagerState = rememberPagerState(initialPage = 0) { 1 }
            val enabled = remember { derivedStateOf { pagerState.currentPage > 0 } }
            PostScreen(
                uuid = uuid,
                imageUrl = imageUrl,
                username = username,
                limit = limit,
                pagerState = pagerState,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                onComment = { postState.value.mapToDetail() },
                modal = {
                    val moderation = LocalModerationInteractor.current
                    TimelineSheet(
                        uuid = uuid,
                        state = showSheet
                    ) { sheetState, post ->
                        when (sheetState) {
                            TimelineSheetMenuItem.REPORT -> moderation.onReport(post.id)
                            TimelineSheetMenuItem.SHARE -> { context.share(post.url, shareTitle) }
                            TimelineSheetMenuItem.BOOST -> { handleBoost(post.id) }
                        }
                    }
                }
            ) {
                GalleryScreen(
                    position = selected.intValue,
                    enabled = enabled,
                    post = postState.value,
                    showSheet = showSheet
                ) {
                    if (uuid != postState.value.author.id) {
                        component.postUserFollow()(
                            modifier = Modifier,
                            spec = PostFollow.Spec(
                                id = postState.value.author.id,
                                isFollowing = postState.value.author.following,
                                isFollowed = postState.value.author.followed
                            )
                        )
                    }
                }
            }
        }
    }
}
