package eu.peernetwork.app.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
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
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.social.ui.connection.ConnectionScreen

@Composable
fun FeedScreen(
    id: String,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStore: UiViewModelStore,
    title: String? = null,
    criteria: Criteria? = null,
    hasUpdate: MutableState<Boolean>,
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
    val ordinal = remember {
        derivedStateOf {
            (state as? FeedViewModel.State.Initialize?)?.filter ?: 0
        }
    }
    val overlay = remember { mutableStateOf<FeedOverlayState>(FeedOverlayState.Empty) }
    val controller = rememberNavController()
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
                FeedPreview(
                    id = id,
                    ordinal = ordinal.value,
                    state = pageState,
                    selected = overlay,
                    requireUpdate = hasUpdate,
                    component = component,
                    viewModelStore = viewModelStore,
                    controller = controller,
                    connectionController = connectionController,
                    title = title,
                    onNavigate = { viewModel.lastVisited(it) },
                    onFilter = { viewModel.setFilter(it) }
                )
            }
        }
    }
}
