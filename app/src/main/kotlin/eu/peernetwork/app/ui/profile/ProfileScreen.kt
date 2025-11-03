package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.social.ui.connection.ConnectionScreen

@Composable
fun ProfileScreen(
    principal: String,
    userId: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    title: String? = null,
) {
    val context = LocalContext.current
    val controller = rememberNavController()
    val component = remember {
        provider.builder(Profile.Builder::class.java).build(context)
    }
    val overlay = remember { mutableStateOf<ProfileOverlayState>(ProfileOverlayState.Empty) }
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { connection ->
        ProfileOverlay(
            overlay = overlay,
            principal = principal,
            userId = userId,
            limit = BuildConfig.PAGING_LIMIT,
            connectionController = connection,
            provider = provider,
            component = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) {
            ProfileNavigation(
                principal = principal,
                userId = userId,
                controller = controller,
                provider = provider,
                component = component
            ) {
                val postState = rememberLazyListState()
                val mediaState = rememberLazyListState()
                ProfilePreview(
                    id = userId,
                    state = overlay,
                    title = title,
                    limit = BuildConfig.PAGING_LIMIT,
                    onSettings = { controller.navigateIfNecessary("settings") },
                    component = component,
                    viewModelStoreOwner = it,
                    postState = postState,
                    mediaState = mediaState,
                    controller = controller
                )
            }
        }
    }
}
