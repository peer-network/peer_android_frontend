package eu.peernetwork.app.ui.feed

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Relation
import eu.peernetwork.blog.ui.timeline.photo.PhotoScreen
import eu.peernetwork.blog.ui.timeline.video.VideoScreen
import eu.peernetwork.core.ui.design.compose.DesignTab
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.social.ui.connection.ConnectionController
import eu.peernetwork.social.ui.connection.ConnectionScreen
import kotlinx.coroutines.launch

@Composable
fun FeedPreview(
    id: String,
    enable: Boolean,
    ordinal: Int,
    state: MutableIntState,
    component: Feed.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    controller: NavHostController,
    connectionController: ConnectionController,
    title: String? = null,
    criteria: Criteria? = null,
    listState: LazyListState,
    onNavigate: (Int) -> Unit = {},
    onFilter: (Int) -> Unit = {},
    onAuthorClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onPhotoClick: (String, Int) -> Unit = { _, _ -> },
    onVideoClick: (String, Int) -> Unit = { _, _ -> },
) {
    val photoState = listState
    val videoState = listState
    val coroutine = rememberCoroutineScope()
    val connection by connectionController.observe().collectAsStateWithLifecycle()
    var relation by rememberSaveable {
        mutableStateOf(Relation.entries.getOrNull(ordinal) ?: Relation.NONE)
    }
    var position by remember { mutableIntStateOf(state.intValue) }
    val handleOnNavigate by rememberUpdatedState(onNavigate)
    val handleOnFilter by rememberUpdatedState(onFilter)
    val pageState = rememberPagerState(
        pageCount = { UiMimeType.TYPES.size },
        initialPage = state.intValue
    )
    FeedPreview(
        state = state,
        pageState = pageState,
        modifier = Modifier.fillMaxSize(),
        onNavigate = {
            position = it
            handleOnNavigate(it)
        },
        photo = {
            PhotoScreen(
                id,
                BuildConfig.PAGING_LIMIT,
                relation,
                criteria,
                onMentionClick,
                onHashtagClick,
                onPhotoClick,
                component,
                viewModelStoreOwner,
                onAuthorClick,
                photoState
            ) {
                ConnectionScreen(
                    isFollowing = connection.getOrDefault(it.first, it.third),
                    isFollowed = it.second,
                    onClick = { follow -> connectionController.invoke(it.first, !follow) },
                )
            }
        },
        video = {
            VideoScreen(
                id,
                enable,
                BuildConfig.PAGING_LIMIT,
                relation,
                criteria,
                { controller.navigateToUsernameSearch(it) },
                { controller.navigateToTagSearch(it) },
                component,
                viewModelStoreOwner,
                onVideoClick,
                { controller.navigateIfNecessary("profile/$it") },
                videoState,
            ) {
                ConnectionScreen(
                    isFollowing = connection.getOrDefault(it.first, it.third),
                    isFollowed = it.second,
                    onClick = { follow -> connectionController.invoke(it.first, !follow) }
                )
            }
        },
    )
    FeedMenu(
        id,
        title,
        relation,
        {
            handleOnFilter(it.ordinal)
            relation = it
        }
    ) {
        coroutine.launch {
            if (position == 0) {
                photoState.animateScrollToItem(0)
            } else {
                videoState.animateScrollToItem(0)
            }
        }
    }
    BackHandler(enabled = pageState.currentPage == 1) {
        coroutine.launch {
            pageState.animateScrollToPage(0)
        }
        state.intValue = 0
        handleOnNavigate(0)
    }
}

@Composable
fun FeedPreview(
    state: MutableIntState,
    pageState: PagerState,
    modifier: Modifier = Modifier,
    onNavigate: (Int) -> Unit = {},
    photo: @Composable () -> Unit,
    video: @Composable () -> Unit,
) {
    val handleNavigation by rememberUpdatedState(onNavigate)
    Column {
        DesignTab(pageState) { index ->
            UiMimeType.get(index)?.let {
                Icon(
                    painter = painterResource(id = it.id),
                    contentDescription = it.label?.let { stringResource(it) },
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(28.dp)
                )
            }
        }
        HorizontalPager(
            state = pageState,
            modifier = modifier,
            verticalAlignment = Alignment.Top,
        ) { page ->
            when (page) {
                0 -> photo()
                1 -> video()
            }
        }
    }
    LaunchedEffect(pageState.currentPage) {
        state.intValue = pageState.currentPage
        handleNavigation(pageState.currentPage)
    }
}

@Composable
@Preview
fun PreviewFeedPreview() {
    val state = rememberSaveable { mutableIntStateOf(0) }
    val pageState = rememberPagerState(pageCount = { UiMimeType.TYPES.size }, initialPage = 0)
    PeerTheme {
        FeedPreview(
            state = state,
            pageState = pageState,
            modifier = Modifier.fillMaxSize(),
            photo = { Text("Photo") },
            video = { Text("Video") },
        )
    }
}
