package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState
import kotlinx.coroutines.launch
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
    val coroutine = rememberCoroutineScope()
    ProfileNavigation(userId, component, viewModelStore) { id, controller ->
        val photoState = rememberLazyListState()
        val videoState = rememberLazyListState()
        ProfilePreview(
            id = id,
            limit = BuildConfig.PAGING_LIMIT,
            onSettings = { controller.navigateIfNecessary("settings") },
            component = component,
            viewModelStoreOwner = viewModelStore.get(id),
            photoState = photoState,
            videoState = videoState,
            onHashtagClick = { controller.navigateToTagSearch(it) },
            onMentionClick = { controller.navigateToUsernameSearch(it) },
            imageOnClick = { controller.navigateIfNecessary("profile/$it") },
        )
        DesignTitleBarHost("ProfileScreen$id", {
            coroutine.launch {
                photoState.animateScrollToItem(0)
                videoState.animateScrollToItem(0)
            }
        }) {
            titleBar {
                DesignTitle {
                    Text(title ?: stringResource(R.string.profile_label))
                }
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
