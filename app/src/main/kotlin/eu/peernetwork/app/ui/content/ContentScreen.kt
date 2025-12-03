package eu.peernetwork.app.ui.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.extension.navigateToTagSearch
import eu.peernetwork.app.extension.navigateToUsernameSearch
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.social.ui.connection.ConnectionInteractor.Companion.LocalConnectionInteractor
import eu.peernetwork.social.ui.connection.ConnectionScreen
import eu.peernetwork.user.domain.model.Account

@Composable
fun ContentScreen(
    account: Account,
    postId: String,
    provider: UiComponentProvider,
    viewModelStore: UiViewModelStore,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Content.Builder::class.java).build(context)
    }
    val controller = rememberNavController()
    val overlay = remember { mutableStateOf<String?>(null) }
    val enabled = remember { derivedStateOf { overlay.value == null } }
//    val event = remember {
//        object : UiPostListener {
//            override fun invoke(event: UiPostListener.Event) {
//                when(event) {
//                    is UiPostListener.Event.Mention ->
//                        controller.navigateToUsernameSearch(event.username)
//                    is UiPostListener.Event.Hashtag ->
//                        controller.navigateToTagSearch(event.tag)
//                    is UiPostListener.Event.Author ->
//                        controller.navigateIfNecessary("profile/${event.id}")
//                    is UiPostListener.Event.Post -> {
//                        overlay.value = event.id
//                    }
//                }
//            }
//        }
//    }
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStore.get(postId)
    ) {
        val connectionController = LocalConnectionInteractor.current
        ContentOverlay(
            overlay = overlay,
            userId = account.id,
            postLimit = BuildConfig.PAGING_LIMIT,
            component = component,
            viewModelStore = viewModelStore,
            connectionController = connectionController
        ) {
            val connection by connectionController.observe().collectAsStateWithLifecycle()
            ContentNavigation(
                account = account,
                postLimit = BuildConfig.PAGING_LIMIT,
                component = component,
                viewModelStore = viewModelStore,
                controller = controller,
                overlay = overlay
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
//                    DetailScreen(
//                        id = postId,
//                        userId = userId,
//                        enabled = enabled,
//                        limit = BuildConfig.PAGING_LIMIT,
//                        event = event,
//                        provider = component,
//                        viewModelStoreOwner = viewModelStore.get(postId),
//                        connection = { relation ->
//                            ConnectionScreen(
//                                isFollowing = connection.getOrDefault(
//                                    key = relation.first,
//                                    defaultValue = relation.third
//                                ),
//                                isFollowed = relation.second,
//                                onClick = { follow ->
//                                    connectionController.value(
//                                        id = relation.first,
//                                        value = !follow
//                                    )
//                                },
//                            )
//                        }
//                    )
                }
            }
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
