package eu.peernetwork.app.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.social.ui.connection.ConnectionController
import eu.peernetwork.social.ui.connection.ConnectionScreen
import java.net.URLEncoder

@Composable
fun ProfileScreen(
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
    val controller = rememberNavController()

    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStore.get(userId)
    ) { connectionController: ConnectionController ->
        ProfileOverlay(
            overlay,
            userId,
            title,
            BuildConfig.PAGING_LIMIT,
            component,
            viewModelStore,
            connectionController
        ) {
            ProfileNavigation(
                userId = userId,
                title = title,
                limit = BuildConfig.PAGING_LIMIT,
                controller = controller,
                component = component,
                viewModelStore = viewModelStore,
                onPhotoClick = { id, index -> },
                onVideoClick = { id, index ->
                    component.videoInteractor().save()
                    overlay.value = ProfileOverlayState.Video(id, index)
                }
            )
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
