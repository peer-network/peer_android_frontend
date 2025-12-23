package eu.peernetwork.app.ui.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.ads.ui.article.ArticleNavigator
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.interactor.NavigationInteractor
import eu.peernetwork.blog.ui.detail.DetailModal
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignOverlay
import eu.peernetwork.user.domain.model.Account

@Composable
fun ContentModal(
    account: Account,
    postId: String,
    selected: MutableIntState,
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
        CompositionLocalProvider(
            PostNavigator.LocalPostNavigator provides navigator,
            ArticleNavigator.LocalArticleNavigator provides navigator,
        ) {
            ContentScreen(
                provider = provider,
                viewModelStoreOwner = viewModelStoreOwner
            ) { component ->
                ContentNavigation(
                    isModal = true,
                    account = account,
                    limit = BuildConfig.PAGING_LIMIT,
                    component = component,
                    controller = controller
                ) {
                    DetailModal(
                        uuid = account.id,
                        postId = postId,
                        username = account.username,
                        imageUrl = account.imageUrl,
                        selected = selected,
                        limit = BuildConfig.PAGING_LIMIT,
                        provider = component,
                        viewModelStoreOwner = viewModelStoreOwner
                    ) { controller.navigate("boost/$it") }
                }
            }
        }
    }
}
