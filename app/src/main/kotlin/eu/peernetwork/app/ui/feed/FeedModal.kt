package eu.peernetwork.app.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.ads.ui.boost.BoostModal
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.interactor.NavigationInteractor
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.blog.ui.timeline.TimelineEvent
import eu.peernetwork.blog.ui.timeline.TimelineModal
import eu.peernetwork.blog.ui.timeline.TimelineScreen
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
    val showBoost = remember { mutableStateOf<String?>(null) }
    DesignOverlay(
        state = isVisible,
        startDestination = "content",
        onDismiss = { isVisible.value = false }
    ) { controller ->
        val key = listOf(category, criteria).hashCode()
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
                    component = component,
                    onCancel = { isVisible.value = false }
                ) {
                    TimelineModal(
                        uuid = account.id,
                        limit = BuildConfig.PAGING_LIMIT,
                        selected = selected,
                        username = account.username,
                        imageUrl = account.imageUrl,
                        category = category,
                        criteria = criteria,
                        provider = component,
                        viewModelStoreOwner = viewModelStoreOwner,
                        onEvent = { event ->
                            when(event) {
                                is TimelineEvent.Post -> {}
                                is TimelineEvent.Boost -> {
                                    showBoost.value = event.id
                                }
                            }
                        }
                    )
                }
                TimelineScreen(
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner
                ) { component, viewModel ->
                    DisposableEffect(Unit) {
                        onDispose {
                            viewModel.selected(key, -1)
                            selected.intValue = -1
                        }
                    }
                }
            }
        }
        BoostModal(
            state = showBoost
        ) { controller.navigate("boost/$it") }
    }
}
