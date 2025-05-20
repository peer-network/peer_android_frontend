package eu.peernetwork.app.ui.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.blog.ui.timeline.photo.PhotoScreen
import eu.peernetwork.blog.ui.timeline.video.VideoScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTab
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.social.ui.connection.ConnectionScreen
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(
    id: String,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStore: ViewModelState,
    title: String? = null,
    criteria: Criteria? = null,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Feed.Builder::class.java).build(context)
    }
    val coroutine = rememberCoroutineScope()
    val viewModelStoreOwner = viewModelStore.get(criteria?.toString() ?: id)
    val viewModel = viewModel(
        modelClass = FeedViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pageState = remember { mutableIntStateOf(state.page) }
    val photoState = rememberLazyListState()
    val videoState = rememberLazyListState()
    FeedNavigation(
        userId = id,
        postLimit = postLimit,
        component = component,
        viewModelStore = viewModelStore,
    ) { controller ->
        ConnectionScreen(
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) { connectionController ->
            val connection by connectionController.observe().collectAsStateWithLifecycle()
            FeedScreen(
                state = pageState,
                modifier = Modifier.fillMaxSize(),
                onNavigate = { viewModel.lastVisited(it) },
                photo = {
                    PhotoScreen(
                        id,
                        BuildConfig.PAGING_LIMIT,
                        criteria,
                        { controller.navigateToUsernameSearch(it) },
                        { controller.navigateToTagSearch(it) },
                        component,
                        viewModelStoreOwner,
                        { controller.navigateIfNecessary("profile/$it") },
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
                        BuildConfig.PAGING_LIMIT,
                        criteria,
                        { controller.navigateToUsernameSearch(it) },
                        { controller.navigateToTagSearch(it) },
                        component,
                        viewModelStoreOwner,
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
            DesignTitleBarHost(
                "FeedScreen$id$title",
                {
                    coroutine.launch {
                        photoState.animateScrollToItem(0)
                        videoState.animateScrollToItem(0)
                    }
                }
            ) {
                titleBar {
                    DesignTitle(modifier = Modifier
                        .clickable(
                            role = Role.Button,
                            onClick = {  })) {
                        Text(title ?: stringResource(R.string.feed_label))
                    }
                }
            }
        }
    }
}

@Composable
fun FeedScreen(
    state: MutableIntState,
    modifier: Modifier = Modifier,
    onNavigate: (Int) -> Unit = {},
    photo: @Composable () -> Unit,
    video: @Composable () -> Unit,
) {
    val pageState = rememberPagerState(
        pageCount = { UiMimeType.TYPES.size },
        initialPage = state.intValue
    )
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
    LaunchedEffect(pageState.currentPage) { onNavigate(pageState.currentPage) }
}

@Composable
@Preview
fun PreviewFeedScreen() {
    val state = rememberSaveable { mutableIntStateOf(0) }
    PeerTheme {
        FeedScreen(
            state = state,
            modifier = Modifier.fillMaxSize(),
            photo = { Text("Photo") },
            video = { Text("Video") },
        )
    }
}
