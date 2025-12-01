package eu.peernetwork.app.ui.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.design.material.DesignOverlay
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.social.ui.connection.ConnectionInteractor

@Composable
fun ContentOverlay(
    overlay: MutableState<String?>,
    userId: String,
    postLimit: Int,
    component: Content.Component,
    viewModelStore: UiViewModelStore,
    connectionController: ConnectionInteractor,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val connection by connectionController.observe().collectAsStateWithLifecycle()
    val visible = remember(overlay.value) {
        mutableStateOf(overlay.value != null)
    }
    updatedContent()
    DesignOverlay(
        startDestination = "overlay",
        state = visible,
        onDismiss = { overlay.value = null }
    ) { controller ->
        val overlayState = remember { mutableStateOf(overlay.value) }
//        val event = remember {
//            object : UiPostListener {
//                override fun invoke(event: UiPostListener.Event) {
//                    when(event) {
//                        is UiPostListener.Event.Mention -> {
//                            controller.navigateToUsernameSearch(event.username)
//                        }
//                        is UiPostListener.Event.Hashtag -> {
//                            controller.navigateToTagSearch(event.tag)
//                        }
//                        is UiPostListener.Event.Author -> {
//                            controller.navigateIfNecessary("profile/${event.id}")
//                        }
//                        else -> {}
//                    }
//                }
//            }
//        }
//        ContentNavigation(
//            userId = userId,
//            postLimit = postLimit,
//            overlay = overlayState,
//            component = component,
//            viewModelStore = viewModelStore,
//            controller = controller,
//            onCancel = { visible.value = false },
//        ) { backStackEntry ->
//            DetailOverlay(
//                id = overlayState.value!!,
//                userId = userId,
//                limit = postLimit,
//                event = event,
//                provider = component,
//                viewModelStoreOwner = viewModelStore.get(overlayState.value!!),
//                header = {
//                    WindowTitle(
//                        provider = component,
//                        viewModelStoreOwner = backStackEntry,
//                        onCancel = { visible.value = false },
//                    )
//                }
//            ) {
//                ConnectionScreen(
//                    isFollowing = connection.getOrDefault(it.first, it.third),
//                    isFollowed = it.second,
//                    onClick = { follow ->
//                        connectionController.value.invoke(it.first, !follow)
//                    }
//                )
//            }
//        }
    }
}
