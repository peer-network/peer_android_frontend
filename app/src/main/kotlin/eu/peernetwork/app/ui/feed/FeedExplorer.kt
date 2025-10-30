package eu.peernetwork.app.ui.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.extension.navigateToTagSearch
import eu.peernetwork.app.extension.navigateToUsernameSearch
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Sort
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.feed.timeline.PostScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.design.luna.DesignTab
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.social.ui.connection.ConnectionController
import eu.peernetwork.social.ui.connection.ConnectionScreen
import kotlinx.coroutines.launch

@Composable
fun FeedExplorer(
    id: String,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStore: UiViewModelStore,
    title: String? = null,
    criteria: Criteria? = null,
    hasUpdate: MutableState<Boolean>,
    onExplore: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val component = remember {
        provider.builder(Feed.Builder::class.java).build(context)
    }
    val viewModelStoreOwner = viewModelStore.get(criteria?.toString() ?: id)
    val overlay = remember { mutableStateOf<FeedOverlayState>(FeedOverlayState.Empty) }
    val controller = rememberNavController()
    val trendListState = rememberLazyListState()
    val latestListState = rememberLazyListState()
    val pageState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { connectionController ->
        FeedOverlay(
            overlay = overlay,
            userId = id,
            criteria = criteria,
            postLimit = postLimit,
            component = component,
            viewModelStore = viewModelStore,
            connectionController = connectionController,
        ) {
            FeedNavigation(
                userId = id,
                postLimit = postLimit,
                controller = controller,
                component = component,
                viewModelStore = viewModelStore,
            ) {
                FeedExplorerTabs(
                    id = id,
                    postLimit = postLimit,
                    selected = overlay,
                    pageState = pageState,
                    requireUpdate = hasUpdate,
                    trendListState = trendListState,
                    latestListState = latestListState,
                    component = component,
                    viewModelStore = viewModelStore,
                    controller = controller,
                    criteria = criteria,
                    connectionController = connectionController,
                    onExplore = onExplore
                )
            }
        }
    }
    DesignTitleBarHost(
        tag = "FeedExplorer",
        listener = {
            scope.launch {
                if (pageState.currentPage == 0) {
                    trendListState.animateScrollToItem(0)
                } else {
                    latestListState.animateScrollToItem(0)
                }
            }
        }
    ) {
        titleBar {
            DesignTitle {
                Text(title ?: stringResource(R.string.explore_label))
            }
        }
    }
}

@Composable
private fun FeedExplorerTabs(
    id: String,
    postLimit: Int,
    pageState: PagerState,
    selected: MutableState<FeedOverlayState>,
    requireUpdate: MutableState<Boolean>,
    trendListState: LazyListState,
    latestListState: LazyListState,
    component: Feed.Component,
    viewModelStore: UiViewModelStore,
    controller: NavHostController,
    criteria: Criteria?,
    connectionController: State<ConnectionController>,
    onExplore: (() -> Unit)? = null
) {
    val sortTypes = listOf(Sort.NEW, Sort.TREND)
    Column {
        DesignTab(pageState) { index ->
            Text(
                text = stringResource(id = if (index == 0) {
                    R.string.latest_label
                } else {
                    R.string.trends_label
                }),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }
        HorizontalPager(
            state = pageState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = androidx.compose.ui.Alignment.Top,
        ) { page ->
            val derivedCriteria = remember { derivedStateOf {
                (criteria as? Criteria.Content?)?.copy(
                    sort = sortTypes[page],
                ) ?: Criteria.Content(sort = sortTypes[page])
            } }
            val enable = remember { derivedStateOf { selected.value == FeedOverlayState.Empty } }
            val connection by connectionController.value.observe().collectAsStateWithLifecycle()
            val event = remember {
                object : UiPostListener {
                    override fun invoke(event: UiPostListener.Event) {
                        when (event) {
                            is UiPostListener.Event.Mention ->
                                controller.navigateToUsernameSearch(event.username)
                            is UiPostListener.Event.Hashtag ->
                                controller.navigateToTagSearch(event.tag)
                            is UiPostListener.Event.Author ->
                                controller.navigateIfNecessary("profile/${event.id}")
                            is UiPostListener.Event.Post -> selected.value = FeedOverlayState.Post(
                                id = event.id,
                                position = event.position,
                                category = Category.NONE,
                                criteria = derivedCriteria.value
                            )
                        }
                    }
                }
            }
            val storeKey = "${Category.NONE};${derivedCriteria.value}"
            PostScreen(
                id = id,
                status = enable,
                postLimit = postLimit,
                category = Category.NONE,
                criteria = derivedCriteria.value,
                event = event,
                provider = component,
                viewModelStoreOwner = viewModelStore.get(storeKey),
                requireUpdate = requireUpdate,
                listState = if (page == 0) trendListState else latestListState,
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
    }
}
