package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import eu.peernetwork.ads.ui.boost.BoostConfirmation
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.interactor.NavigationInteractor
import eu.peernetwork.blog.domain.usecase.PostUsecase
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.user.domain.model.Account

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
    account: Account,
    userId: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    title: String? = null,
) {
    val context = LocalContext.current
    val controller = rememberNavController()
    val isVisible = remember { mutableStateOf(false) }
    val selected = remember { mutableIntStateOf(-1) }
    val timestamp = rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    val navigator = remember { NavigationInteractor(context, controller) }
    CompositionLocalProvider(
        PostNavigator.LocalPostNavigator provides navigator
    ) {
        ProfileScreen(provider) { component ->
            val showBoost = remember { mutableStateOf<String?>(null) }
            val pageState = rememberPagerState(
                pageCount = { UiMimeType.TYPES.size },
                initialPage = 0
            )
            ProfileNavigation(
                account = account,
                controller = controller,
                component = component,
                viewModelStoreOwner = viewModelStoreOwner
            ) {
                val postState = rememberLazyListState()
                val mediaState = rememberLazyListState()
                ProfilePage(
                    uuid = account.id,
                    user = userId,
                    username = account.username,
                    imageUrl = account.imageUrl,
                    title = title,
                    limit = BuildConfig.PAGING_LIMIT,
                    selected = selected,
                    isVisible = isVisible,
                    timestamp = timestamp,
                    onSettings = { controller.navigateIfNecessary("settings") },
                    component = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    postState = postState,
                    mediaState = mediaState,
                    controller = controller,
                    pageState = pageState,
                    onBoost = { showBoost.value = it },
                    onClick = { isVisible.value = true }
                )
            }
            ProfileModal(
                account = account,
                userId = userId,
                isVisible = isVisible,
                selected = selected,
                timestamp = timestamp,
                provider = provider,
                types = if (pageState.currentPage == 0) {
                    PostUsecase.POST
                } else {
                    PostUsecase.MEDIA
                },
                viewModelStoreOwner = viewModelStoreOwner
            )
            BoostConfirmation(
                state = showBoost
            ) { controller.navigate("boost/$it") }
        }
    }
}
