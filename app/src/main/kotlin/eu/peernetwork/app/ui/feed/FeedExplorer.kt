package eu.peernetwork.app.ui.feed

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.extension.navigateToTagSearch
import eu.peernetwork.app.extension.navigateToUsernameSearch
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.feed.timeline.PostScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.social.ui.connection.ConnectionController
import eu.peernetwork.social.ui.connection.ConnectionScreen

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
    val component = remember {
        provider.builder(Feed.Builder::class.java).build(context)
    }
    val viewModelStoreOwner = viewModelStore.get(criteria?.toString() ?: id)
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
                FeedExplorer(
                    id = id,
                    postLimit = postLimit,
                    selected = overlay,
                    requireUpdate = hasUpdate,
                    component = component,
                    viewModelStore = viewModelStore,
                    controller = controller,
                    connectionController = connectionController,
                    criteria = criteria,
                    onExplore = onExplore
                )
            }
        }
    }
    DesignTitleBarHost("FeedExplorer") {
        titleBar {
            DesignTitle {
                Text(title ?: stringResource(R.string.explore_label))
            }
        }
    }
}

@Composable
fun FeedExplorer(
    id: String,
    postLimit: Int,
    selected: MutableState<FeedOverlayState>,
    requireUpdate: MutableState<Boolean>,
    component: Feed.Component,
    viewModelStore: UiViewModelStore,
    controller: NavHostController,
    connectionController: State<ConnectionController>,
    criteria: Criteria? = null,
    onExplore: (() -> Unit)? = null
) {
    val listState = rememberLazyListState()
    val enable = remember { derivedStateOf { selected.value == FeedOverlayState.Empty } }
    val connection by connectionController.value.observe().collectAsStateWithLifecycle()
    val category = Category.NONE
    val event = remember {
        object : UiPostListener {
            override fun invoke(event: UiPostListener.Event) {
                when(event) {
                    is UiPostListener.Event.Mention -> controller.navigateToUsernameSearch(event.username)
                    is UiPostListener.Event.Hashtag -> controller.navigateToTagSearch(event.tag)
                    is UiPostListener.Event.Author -> controller.navigateIfNecessary("profile/${event.id}")
                    is UiPostListener.Event.Post -> selected.value = FeedOverlayState.Post(
                        id = event.id,
                        position = event.position,
                        category = category
                    )
                }
            }
        }
    }
    val storeKey = "$category;${criteria?.toString() ?: id}"
    PostScreen(
        id = id,
        status = enable,
        postLimit = postLimit,
        category = category,
        criteria = criteria,
        event = event,
        provider = component,
        viewModelStoreOwner = viewModelStore.get(storeKey),
        requireUpdate = requireUpdate,
        listState = listState,
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