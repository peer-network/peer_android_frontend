package eu.peernetwork.app.ui.profile.v2

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.app.ui.profile.ProfileSheet
import eu.peernetwork.blog.ui.article.ArticleScreenEvent
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.social.ui.connection.ConnectionStatus
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProfilePage(
    id: String,
    title: String?,
    limit: Int,
    onSettings: () -> Unit = {},
    onClick: () -> Unit = {},
    postState: LazyListState = rememberLazyListState(),
    mediaState: LazyListState = rememberLazyListState(),
    component: Profile.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    controller: NavHostController,
) {
    val requireUpdate = rememberSaveable { mutableStateOf(false) }
    val timestamp = rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    val requirePostUpdate = rememberSaveable { mutableStateOf(false) }
    val connection = remember { mutableStateOf<ConnectionStatus?>(null) }
    val showSheet = remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    val pageState = rememberPagerState(
        pageCount = { UiMimeType.TYPES.size },
        initialPage = 0
    )
    val handleClick by rememberUpdatedState(onClick)
    ProfileScaffold(
        pageState = pageState,
        onRefresh = {
            timestamp.longValue = System.currentTimeMillis()
            requireUpdate.value = true
            requirePostUpdate.value = true
        },
        header = {
            ProfileDetail(
                id = id,
                timestamp = timestamp,
                showSheet = showSheet,
                onSettings = onSettings,
                onMenuClicked = { controller.navigate("adverts") },
                component = component,
                viewModelStoreOwner = viewModelStoreOwner
            )
        },
    ) {
        ProfilePosts(
            id = id,
            page = it,
            limit = limit,
            timestamp = timestamp,
            component = component,
            viewModelStoreOwner = viewModelStoreOwner,
            postState = postState,
            mediaState = mediaState
        ) { event ->
            when(event) {
                is ArticleScreenEvent.Boost -> {
                    controller.navigate("boost/${event.id}")
                }
                is ArticleScreenEvent.Post -> {
                    handleClick()
                }
            }
        }
    }
    ProfileSheet(
        id = id,
        state = showSheet,
        limit = limit,
        status = connection,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { controller.navigateIfNecessary("profile/${it.id}") }
    DesignTitleBarHost("ProfileScreen$id", {
        coroutine.launch {
            if (pageState.currentPage == 0) {
                postState.animateScrollToItem(0)
            } else {
                mediaState.animateScrollToItem(0)
            }
        }
    }) {
        titleBar {
            DesignTitle {
                Text(title ?: stringResource(R.string.profile_label))
            }
        }
    }
}
