package eu.peernetwork.app.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.ui.timeline.TimelineModal
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignOverlay
import eu.peernetwork.user.domain.model.Account

@Composable
fun FeedModal(
    account: Account,
    selected: MutableIntState,
    category: Category,
    criteria: Criteria = Criteria.None,
    isVisible: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val controller = rememberNavController()
    DesignOverlay(
        state = isVisible,
        onDismiss = { isVisible.value = false }
    ) {
        FeedScreen(
            provider = provider,
            viewModelStoreOwner = viewModelStoreOwner
        ) { component, viewModel ->
            FeedNavigation(
                account = account,
                controller = controller,
                component = component
            ) {
                TimelineModal(
                    id = account.id,
                    limit = BuildConfig.PAGING_LIMIT,
                    selected = selected,
                    username = account.username,
                    imageUrl = account.imageUrl,
                    category = category,
                    criteria = criteria,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner
                )
            }
        }
    }
}
