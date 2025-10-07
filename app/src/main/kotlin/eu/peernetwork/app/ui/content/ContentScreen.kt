package eu.peernetwork.app.ui.content

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.extension.navigateToTagSearch
import eu.peernetwork.app.extension.navigateToUsernameSearch
import eu.peernetwork.blog.ui.content.detail.DetailScreen
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.social.ui.connection.ConnectionScreen

@Composable
fun ContentScreen(
    userId: String,
    postId: String,
    provider: UiComponentProvider,
    viewModelStore: UiViewModelStore,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Content.Builder::class.java).build(context)
    }
    val controller = rememberNavController()
    val event = remember {
        object : UiPostListener {
            override fun invoke(event: UiPostListener.Event) {
                when(event) {
                    is UiPostListener.Event.Mention ->
                        controller.navigateToUsernameSearch(event.username)
                    is UiPostListener.Event.Hashtag ->
                        controller.navigateToTagSearch(event.tag)
                    is UiPostListener.Event.Author ->
                        controller.navigateIfNecessary("profile/${event.id}")
                    is UiPostListener.Event.Post -> {}
                }
            }
        }
    }
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStore.get(postId)
    ) { connectionController ->
        val connection by connectionController.value.observe().collectAsStateWithLifecycle()
        ContentNavigation(
            userId = userId,
            postLimit = BuildConfig.PAGING_LIMIT,
            component = component,
            viewModelStore = viewModelStore,
            controller = controller
        ) {
            DetailScreen(
                id = postId,
                userId = userId,
                limit = BuildConfig.PAGING_LIMIT,
                event = event,
                provider = component,
                viewModelStoreOwner = viewModelStore.get(postId),
                connection = { relation ->
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
            )
        }
    }
    DesignTitleBarHost("ContentScreen$postId") {
        titleBar {
            DesignTitle {
                Text(stringResource(eu.peernetwork.blog.ui.R.string.post_label))
            }
        }
    }
}
