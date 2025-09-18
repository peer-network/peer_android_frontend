package eu.peernetwork.app.ui.feed

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.extension.navigateToTagSearch
import eu.peernetwork.app.extension.navigateToUsernameSearch
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.feed.timeline.PostScreen
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.social.ui.connection.ConnectionController
import eu.peernetwork.social.ui.connection.ConnectionScreen

@Composable
fun FeedExplorer(
    id: String,
    state: MutableIntState,
    selected: MutableState<FeedOverlayState>,
    requireUpdate: MutableState<Boolean>,
    component: Feed.Component,
    viewModelStore: UiViewModelStore,
    controller: NavHostController,
    connectionController: State<ConnectionController>,
    criteria: Criteria? = null,
    onNavigate: (Int) -> Unit = {},
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

    PostScreen(
        id = id,
        status = enable,
        postLimit = BuildConfig.PAGING_LIMIT,
        category = category,
        criteria = criteria,
        event = event,
        provider = component,
        viewModelStoreOwner = viewModelStore.get("explore;$id"),
        requireUpdate = requireUpdate,
        listState = listState,
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