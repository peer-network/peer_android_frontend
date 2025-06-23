package eu.peernetwork.app.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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
    criteria: Criteria? = null
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Feed.Builder::class.java).build(context)
    }
    val viewModelStoreOwner = viewModelStore.get(criteria?.toString() ?: id)
    val viewModel = viewModel(
        modelClass = FeedViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pageState = remember { mutableIntStateOf(state.page) }
    val overlay = remember { mutableStateOf<FeedOverlayState>(FeedOverlayState.Empty) }
    val controller = rememberNavController()
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
                    onPhotoClick = { id, index -> },
                    onVideoClick = { id, index ->
                        component.videoInteractor().save()
                        overlay.value = FeedOverlayState.Video(id, index) }
                )
            }
        }
    }
}
