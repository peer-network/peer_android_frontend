package eu.peernetwork.app.ui.profile.v2

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.social.ui.connection.ConnectionController
import eu.peernetwork.social.ui.connection.ConnectionScreen

@Composable
fun ProfileScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Profile.Component, State<ConnectionController>) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Profile.Builder::class.java).build(context)
    }
    val updatedContent by rememberUpdatedState(content)
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { connection ->
        updatedContent(component, connection)
    }
}

@Composable
fun ProfileScreen(
    principal: String,
    userId: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    title: String? = null,
) {
    val controller = rememberNavController()
    val isVisible = remember { mutableStateOf(false) }
    ProfileScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, connection ->
        ProfileNavigation(
            principal = principal,
            userId = userId,
            controller = controller,
            provider = provider,
            component = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) {
            val postState = rememberLazyListState()
            val mediaState = rememberLazyListState()
            ProfilePreview(
                id = userId,
                title = title,
                limit = BuildConfig.PAGING_LIMIT,
                onSettings = { controller.navigateIfNecessary("settings") },
                component = component,
                viewModelStoreOwner = viewModelStoreOwner,
                postState = postState,
                mediaState = mediaState,
                controller = controller,
                onClick = { isVisible.value = true }
            )
        }
        ProfileOverlay(
            principal = principal,
            userId = userId,
            isVisible = isVisible,
            provider = provider,
            viewModelStoreOwner = viewModelStoreOwner
        )
    }
}
