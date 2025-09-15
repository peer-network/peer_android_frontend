package eu.peernetwork.app.ui.feed

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.extension.navigateToTagSearch
import eu.peernetwork.app.extension.navigateToUsernameSearch
import eu.peernetwork.app.mapper.mapToCriteria
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.feed.photo.PostScreen
import eu.peernetwork.blog.ui.model.UiFilter
import eu.peernetwork.core.ui.design.compose.DesignTab
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.social.ui.connection.ConnectionController
import eu.peernetwork.social.ui.connection.ConnectionScreen
import kotlinx.coroutines.launch

@Composable
fun FeedPreview(
    id: String,
    ordinal: Int,
    state: MutableIntState,
    selected: MutableState<FeedOverlayState>,
    requireUpdate: MutableState<Boolean>,
    component: Feed.Component,
    viewModelStore: UiViewModelStore,
    controller: NavHostController,
    connectionController: State<ConnectionController>,
    title: String? = null,
    criteria: Criteria? = null,
    onNavigate: (Int) -> Unit = {},
    onFilter: (Int) -> Unit = {},
) {
    val postState = rememberLazyListState()
    val mediaState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()
    val enable = remember { derivedStateOf { selected.value == FeedOverlayState.Empty } }
    val filter = remember(criteria) { mutableStateOf(criteria) }
    val derivedCriteria = remember(ordinal, filter.value) { derivedStateOf {
        filter.value ?: UiFilter.entries.getOrNull(ordinal)?.mapToCriteria(
            sort = criteria?.sort,
            tag = criteria?.tag,
            title = criteria?.title
        )
    } }
    val connection by connectionController.value.observe().collectAsStateWithLifecycle()
    var position by remember { mutableIntStateOf(state.intValue) }
    val handleOnNavigate by rememberUpdatedState(onNavigate)
    val handleOnFilter by rememberUpdatedState(onFilter)
    val pageState = rememberPagerState(
        pageCount = { UiMimeType.TYPES.size },
        initialPage = state.intValue
    )
    val event = remember {
        object : UiPostListener {
            override fun onMentionClick(username: String) = controller.navigateToUsernameSearch(username)

            override fun onHashtagClick(tag: String) = controller.navigateToTagSearch(tag)

            override fun onPostClick(id: String, position: Int) {
                if (pageState.currentPage == 0) {
                    selected.value = FeedOverlayState.Post(id, position)
                } else {
                    selected.value = FeedOverlayState.Media(id, position)
                }
            }

            override fun onAuthorClick(id: String) = controller.navigateIfNecessary("profile/$id")
        }
    }
    FeedPreview(
        state = state,
        pageState = pageState,
        modifier = Modifier.fillMaxSize(),
        onNavigate = {
            position = it
            handleOnNavigate(it)
        },
        post = {
            val storeKey = "${Category.FOLLOWER};${criteria?.toString() ?: id}"
            PostScreen(
                id = id,
                status = enable,
                postLimit = BuildConfig.PAGING_LIMIT,
                category = Category.FOLLOWER,
                criteria = derivedCriteria.value,
                event = event,
                provider = component,
                viewModelStoreOwner = viewModelStore.get(storeKey),
                requireUpdate = requireUpdate,
                listState = postState,
            ) {
                ConnectionScreen(
                    isFollowing = connection.getOrDefault(it.first, it.third),
                    isFollowed = it.second,
                    onClick = { follow ->
                        connectionController.value(it.first, !follow)
                    },
                )
            }
        },
        media = {
            val storeKey = "${Category.FOLLOWED};${criteria?.toString() ?: id}"
            PostScreen(
                id = id,
                status = enable,
                postLimit = BuildConfig.PAGING_LIMIT,
                category = Category.FOLLOWED,
                criteria = derivedCriteria.value,
                event = event,
                provider = component,
                viewModelStoreOwner = viewModelStore.get(storeKey),
                requireUpdate = requireUpdate,
                listState = mediaState,
            ) {
                ConnectionScreen(
                    isFollowing = connection.getOrDefault(it.first, it.third),
                    isFollowed = it.second,
                    onClick = { follow ->
                        connectionController.value(it.first, !follow)
                    },
                )
            }
        },
    )
    FeedMenu(
        id = id,
        default = ordinal,
        title = title,
        onSelect = { ordinal, criteria ->
            handleOnFilter(ordinal)
            filter.value = criteria
        }
    ) {
        coroutine.launch {
            if (position == 0) {
                postState.animateScrollToItem(0)
            } else {
                mediaState.animateScrollToItem(0)
            }
        }
    }
    BackHandler(enabled = pageState.currentPage == 1 && enable.value) {
        coroutine.launch { pageState.animateScrollToPage(0) }
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
    post: @Composable () -> Unit,
    media: @Composable () -> Unit,
) {
    val handlePost by rememberUpdatedState(post)
    val handleMedia by rememberUpdatedState(media)
    val handleNavigation by rememberUpdatedState(onNavigate)
    Column {
        DesignTab(pageState) { index ->
            UiMimeType.get(index)?.let { type ->
                Icon(
                    painter = painterResource(id = type.id),
                    contentDescription = type.label?.let { stringResource(it) },
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
                0 -> handlePost()
                1 -> handleMedia()
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
            post = { Text("Photo") },
            media = { Text("Video") },
        )
    }
}
