package eu.peernetwork.app.ui.feed

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.social.ui.connection.ConnectionScreen

@Composable
fun FeedScreen(
    id: String,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStore: ViewModelState,
    title: String? = null,
    controller: NavHostController = rememberNavController(),
    criteria: Criteria? = null
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Feed.Builder::class.java).build(context)
    }
    val viewModelStoreOwner = viewModelStore.get(criteria?.toString() ?: id)
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val scrollToTop = remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    LaunchedEffect(navBackStackEntry) {
        scrollToTop.value = true }
    LaunchedEffect(scrollToTop.value) {
        if (scrollToTop.value) {
            listState.scrollToItem(0)
            scrollToTop.value = false }
    }
    val viewModel = viewModel(
        modelClass = FeedViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pageState = remember { mutableIntStateOf(state.page) }
    val ordinal = remember {
        derivedStateOf {
            (state as? FeedViewModel.State.Initialize?)?.filter ?: 0
        }
    }
    val overlay = remember { mutableStateOf<FeedOverlayState>(FeedOverlayState.Empty) }

    FeedOverlay(
        overlay = overlay,
        userId = id,
        postLimit = postLimit,
        component = component,
        viewModelStore = viewModelStore,
    ) {
        FeedNavigation(
            userId = id,
            postLimit = postLimit,
            controller = controller,
            component = component,
            viewModelStore = viewModelStore,
        ) {
            ConnectionScreen(
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            ) { connectionController ->
                FeedPreview(
                    id = id,
                    enable = overlay.value == FeedOverlayState.Empty,
                    ordinal = ordinal.value,
                    state = pageState,
                    component = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    controller = controller,
                    connectionController = connectionController,
                    criteria = criteria,
                    onMentionClick = { controller.navigateToUsernameSearch(it) },
                    onHashtagClick = { controller.navigateToTagSearch(it) },
                    onAuthorClick = { controller.navigateIfNecessary("profile/$it") },
                    title = title,
                    onNavigate = { viewModel.lastVisited(it) },
                    onFilter = { viewModel.setFilter(it) },
                    onPhotoClick = { id, index -> },
                    onVideoClick = { id, index ->
                        overlay.value = FeedOverlayState.Video(id, index)
                    }, listState = listState
                )
            }
        }
    }
}
