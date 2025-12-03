package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary

@Composable
fun ProfileScreen(
    provider: UiComponentProvider,
    content: @Composable (Profile.Component) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Profile.Builder::class.java).build(context)
    }
    val updatedContent by rememberUpdatedState(content)
    updatedContent(component)
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
    val selected = remember { mutableIntStateOf(-1) }
    val timestamp = rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    ProfileScreen(provider) { component ->
        ProfileNavigation(
            principal = principal,
            userId = userId,
            controller = controller,
            component = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) {
            val postState = rememberLazyListState()
            val mediaState = rememberLazyListState()
            ProfilePage(
                id = userId,
                title = title,
                limit = BuildConfig.PAGING_LIMIT,
                selected = selected,
                timestamp = timestamp,
                onSettings = { controller.navigateIfNecessary("settings") },
                component = component,
                viewModelStoreOwner = viewModelStoreOwner,
                postState = postState,
                mediaState = mediaState,
                controller = controller,
                onClick = { isVisible.value = true }
            )
        }
        ProfileModal(
            principal = principal,
            userId = userId,
            isVisible = isVisible,
            selected = selected,
            timestamp = timestamp,
            provider = provider,
            viewModelStoreOwner = viewModelStoreOwner
        )
    }
}
