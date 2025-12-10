package eu.peernetwork.app.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.ads.ui.boost.BoostConfirmation
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.interactor.NavigationInteractor
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.article.ArticleEvent
import eu.peernetwork.blog.ui.article.ArticleModal
import eu.peernetwork.blog.ui.article.ArticleScreen
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignOverlay
import eu.peernetwork.user.domain.model.Account

@Composable
fun ProfileModal(
    account: Account,
    userId: String,
    types: Set<Content.Type>,
    selected: MutableIntState,
    isVisible: MutableState<Boolean>,
    timestamp: State<Long>,
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
        val key = "${types.hashCode()}/$userId"
        val navigator = remember { NavigationInteractor(context, controller) }
        CompositionLocalProvider(PostNavigator.LocalPostNavigator provides navigator) {
            ProfileScreen(provider) { component ->
                ProfileNavigation(
                    account = account,
                    isModal = true,
                    controller = controller,
                    component = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    onCancel = { isVisible.value = false }
                ) {
                    ArticleModal(
                        id = account.id,
                        author = userId,
                        username = account.username,
                        imageUrl = account.imageUrl,
                        types = types,
                        limit = BuildConfig.PAGING_LIMIT,
                        selected = selected,
                        timestamp = timestamp,
                        provider = component,
                        viewModelStoreOwner = viewModelStoreOwner
                    ) { event ->
                        when(event) {
                            is ArticleEvent.Boost -> showBoost.value = event.id
                            is ArticleEvent.Post -> {}
                        }
                    }
                }
                ArticleScreen(
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
        BoostConfirmation(
            state = showBoost
        ) { controller.navigate("boost/$it") }
    }
}
