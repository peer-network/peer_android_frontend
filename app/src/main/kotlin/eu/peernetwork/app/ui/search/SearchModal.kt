package eu.peernetwork.app.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.blog.ui.explore.ExploreModal
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
    val controller = rememberNavController()
    DesignOverlay(
        state = isVisible,
        onDismiss = { isVisible.value = false }
    ) {
        SearchScreen(
            provider = provider,
            viewModelStoreOwner = viewModelStoreOwner
        ) { component ->
            SearchNavigation(
                account = account,
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
        }
    }
}
