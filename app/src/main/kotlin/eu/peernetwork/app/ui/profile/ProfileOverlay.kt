package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignOverlay
import eu.peernetwork.core.ui.design.material.DesignOverlayPage
import eu.peernetwork.social.ui.connection.ConnectionController

sealed interface ProfileOverlayState {
    data object Empty : ProfileOverlayState

    data class Photo(
        val id: String,
        val position: Int,
        val page: Int
    ) : ProfileOverlayState
}

@Composable
fun ProfileOverlay(
    overlay: MutableState<ProfileOverlayState>,
    principal: String,
    userId: String,
    limit: Int,
    connectionController: State<ConnectionController>,
    provider: UiComponentProvider,
    component: Profile.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val connection by connectionController.value.observe().collectAsStateWithLifecycle()
    val visible = remember(overlay.value) {
        mutableStateOf(overlay.value !is ProfileOverlayState.Empty)
    }
    updatedContent()
    DesignOverlay(
        startDestination = "overlay",
        state = visible,
        onDismiss = { overlay.value = ProfileOverlayState.Empty }
    ) { controller ->
        DesignOverlayPage(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            val overlayState = remember { mutableStateOf<ProfileOverlayState?>(overlay.value) }
//            val event = remember {
//                object : UiPostListener {
//                    override fun invoke(event: UiPostListener.Event) {
//                        when(event) {
//                            is UiPostListener.Event.Mention -> {
//                                controller.navigateToUsernameSearch(event.username)
//                            }
//                            is UiPostListener.Event.Hashtag -> {
//                                controller.navigateToTagSearch(event.tag)
//                            }
//                            is UiPostListener.Event.Author -> {
//                                controller.navigateIfNecessary("profile/${event.id}")
//                            }
//                            else -> {}
//                        }
//                    }
//                }
//            }
            ProfileNavigation(
                principal = principal,
                userId = userId,
                startDestination = "overlay",
                controller = controller,
                provider = provider,
                component = component,
                viewModelStoreOwner = viewModelStoreOwner,
                onCancel = { visible.value = false }
            ) { backStackEntry ->
                val state = (overlayState.value as ProfileOverlayState.Photo)
//                ArticleOverlay(
//                    author = userId,
//                    types = PostUsecase.POST,
//                    enabled = visible.value,
//                    limit = limit,
//                    position = state.position,
//                    provider = component,
//                    viewModelStoreOwner = viewModelStoreOwner,
//                    header = {
//                        WindowTitle(
//                            provider = component,
//                            viewModelStoreOwner = backStackEntry,
//                            onCancel = { visible.value = false },
//                        )
//                    }
//                ) {
//                    if (userId != principal) {
//                        ConnectionScreen(
//                            isFollowing = connection.getOrDefault(it.first, it.third),
//                            isFollowed = it.second,
//                            onClick = { follow ->
//                                connectionController.value.invoke(it.first, !follow)
//                            }
//                        )
//                    }
//                }
            }
        }
    }
}
