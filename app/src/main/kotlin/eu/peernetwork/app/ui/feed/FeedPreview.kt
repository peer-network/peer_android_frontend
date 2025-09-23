package eu.peernetwork.app.ui.feed

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import eu.peernetwork.core.ui.R
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import eu.peernetwork.blog.ui.feed.timeline.PostScreen
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
    onExplore: () -> Unit,
) {
    val followerListState = rememberLazyListState()
    val followedListState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()
    val enable = remember { derivedStateOf { selected.value == FeedOverlayState.Empty } }
    val filter = remember(criteria) { mutableStateOf(criteria) }
    val derivedCriteria = remember(ordinal, filter.value) { derivedStateOf {
        val content = criteria as? Criteria.Content?
        filter.value ?: UiFilter.entries.getOrNull(ordinal)?.mapToCriteria(
            tag = content?.tag,
            title = content?.title
        )
    } }
    val connection by connectionController.value.observe().collectAsStateWithLifecycle()
    val handleOnNavigate by rememberUpdatedState(onNavigate)
    val handleOnFilter by rememberUpdatedState(onFilter)
    val pageState = rememberPagerState(
        pageCount = { UiMimeType.TYPES.size },
        initialPage = state.intValue
    )
    val event = remember {
        object : UiPostListener {
            override fun invoke(event: UiPostListener.Event) {
                when(event) {
                    is UiPostListener.Event.Mention -> {
                        controller.navigateToUsernameSearch(event.username)
                    }
                    is UiPostListener.Event.Hashtag -> {
                        controller.navigateToTagSearch(event.tag)
                    }
                    is UiPostListener.Event.Author -> {
                        controller.navigateIfNecessary("profile/${event.id}")
                    }
                    is UiPostListener.Event.Post -> {
                        selected.value = FeedOverlayState.Post(
                            id = event.id,
                            position = event.position,
                            criteria = derivedCriteria.value,
                            category = if (pageState.currentPage == 0) {
                                Category.FOLLOWER
                            } else {
                                Category.FOLLOWED
                            }
                        )
                    }
                }
            }
        }
    }
    FeedPreview(
        state = state,
        pageState = pageState,
        modifier = Modifier.fillMaxSize(),
        onNavigate = { handleOnNavigate(it) }
    ) {
        val storeKey = "$it;${criteria?.toString() ?: id}"
        PostScreen(
            id = id,
            status = enable,
            postLimit = BuildConfig.PAGING_LIMIT,
            category = it,
            criteria = derivedCriteria.value,
            event = event,
            provider = component,
            viewModelStoreOwner = viewModelStore.get(storeKey),
            requireUpdate = requireUpdate,
            listState = if (it == Category.FOLLOWER) {
                followerListState
            } else {
                followedListState
            },
            onExplore = onExplore
        ) { relation ->
            ConnectionScreen(
                isFollowing = connection.getOrDefault(
                    key = relation.first,
                    defaultValue = relation.third
                ),
                isFollowed = relation.second,
                onClick = { follow ->
                    connectionController.value(
                        id = relation.first,
                        value = !follow
                    )
                },
            )
        }
    }
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
            if (pageState.currentPage == 0) {
                followerListState.animateScrollToItem(0)
            } else {
                followedListState.animateScrollToItem(0)
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
    content: @Composable (Category) -> Unit,
) {
    val handleContent by rememberUpdatedState(content)
    val handleNavigation by rememberUpdatedState(onNavigate)
    Column {
        DesignTab(pageState) { index ->
            Text(
                text = stringResource(id = if (index == 0) R.string.followers_label else R.string.following_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
        }
        HorizontalPager(
            state = pageState,
            modifier = modifier,
            verticalAlignment = Alignment.Top,
        ) { page ->
            when (page) {
                0 -> handleContent(Category.FOLLOWER)
                1 -> handleContent(Category.FOLLOWED)
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
            modifier = Modifier.fillMaxSize()
        ) { Text("Video") }
    }
}
