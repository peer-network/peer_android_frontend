package eu.peernetwork.app.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.interactor.NavigationInteractor
import eu.peernetwork.blog.ui.explore.ExploreModal
import eu.peernetwork.blog.ui.explore.ExploreScreen
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignOverlay
import eu.peernetwork.user.domain.model.Account

@Composable
fun SearchModal(
    account: Account,
    selected: MutableIntState,
    isVisible: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    DesignOverlay(
        state = isVisible,
        startDestination = "search",
        onDismiss = { isVisible.value = false }
    ) { controller ->
        val navigator = remember { NavigationInteractor(context, controller) }
        CompositionLocalProvider(PostNavigator.LocalPostNavigator provides navigator) {
            SearchScreen(
                provider = provider,
                viewModelStoreOwner = viewModelStoreOwner
            ) { component ->
                SearchNavigation(
                    account = account,
                    isModal = true,
                    controller = controller,
                    component = component
                ) {
                    ExploreModal(
                        limit = BuildConfig.PAGING_LIMIT,
                        selected = selected,
                        provider = component,
                        imageUrl = account.imageUrl,
                        username = account.username,
                        viewModelStoreOwner = viewModelStoreOwner
                    )
                }
                ExploreScreen(
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner
                ) { component, viewModel ->
                    val key = component.hashCode()
                    DisposableEffect(Unit) {
                        onDispose {
                            viewModel.selected(key, -1)
                            selected.intValue = -1
                        }
                    }
                }
            }
        }
    }
}
