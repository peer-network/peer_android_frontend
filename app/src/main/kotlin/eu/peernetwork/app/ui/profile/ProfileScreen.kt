package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.social.ui.connection.ConnectionScreen
import java.net.URLEncoder

@Composable
fun ProfileScreen(
    principal: String,
    userId: String,
    provider: UiComponentProvider,
    viewModelStore: ViewModelState,
    title: String? = null,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Profile.Builder::class.java).build(context)
    }
    val overlay = remember { mutableStateOf<ProfileOverlayState>(ProfileOverlayState.Empty) }
    val enable =  remember { derivedStateOf { overlay.value == ProfileOverlayState.Empty } }
    val controller = rememberNavController()
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStore.get(userId)
    ) { connection ->
        ProfileOverlay(
            overlay = overlay,
            principal = principal,
            userId = userId,
            limit = BuildConfig.PAGING_LIMIT,
            connectionController = connection,
            provider = provider,
            component = component,
            viewModelStore = viewModelStore
        ) {
            ProfileNavigation(
                principal = principal,
                userId = userId,
                controller = controller,
                provider = provider,
                component = component,
                viewModelStore = viewModelStore,
            ) {
                val photoState = rememberLazyListState()
                val videoState = rememberLazyListState()
                ProfilePreview(
                    id = userId,
                    enable = enable,
                    title = title,
                    limit = BuildConfig.PAGING_LIMIT,
                    onSettings = { controller.navigateIfNecessary("settings") },
                    component = component,
                    viewModelStoreOwner = viewModelStore.get(userId),
                    photoState = photoState,
                    videoState = videoState,
                    onPhotoClick = { id, index ->
                        overlay.value = ProfileOverlayState.Photo(id, index)
                    },
                    onVideoClick = { id, index ->
                        overlay.value = ProfileOverlayState.Video(id, index) },
                    onHashtagClick = { controller.navigateToTagSearch(it) },
                    onMentionClick = { controller.navigateToUsernameSearch(it) },
                    onAuthorClicked = { controller.navigateIfNecessary("profile/$it") },
                )
            }
        }
    }
}

fun NavHostController.navigateToTagSearch(tag: String) {
    val cleanTag = tag.removePrefix("#")
    val encoded = URLEncoder.encode(cleanTag, "UTF-8")
    navigate("search/tag/$encoded") {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToUsernameSearch(username: String) {
    val cleanUsername = username.removePrefix("@")
    val encoded = URLEncoder.encode(cleanUsername, "UTF-8")
    navigate("search/username/$encoded") {
        launchSingleTop = true
    }
}
