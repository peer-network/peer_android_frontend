package eu.peernetwork.app.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.interactor.NavigationInteractor
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.ui.post.PostNavigator
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
    val context = LocalContext.current
    DesignOverlay(
        state = isVisible,
        startDestination = "content",
        onDismiss = { isVisible.value = false }
    ) { controller ->
        val navigator = remember { NavigationInteractor(context, controller) }
        CompositionLocalProvider(PostNavigator.LocalPostNavigator provides navigator) {
            FeedScreen(
                provider = provider,
                viewModelStoreOwner = viewModelStoreOwner
            ) { component, viewModel ->
                FeedNavigation(
                    account = account,
                    isModal = true,
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
}
