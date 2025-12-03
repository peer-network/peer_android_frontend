package eu.peernetwork.app.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.article.ArticleModal
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
    val controller = rememberNavController()
    DesignOverlay(
        state = isVisible,
        onDismiss = { isVisible.value = false }
    ) {
        ProfileScreen(provider) { component ->
            ProfileNavigation(
                account = account,
                controller = controller,
                component = component,
                viewModelStoreOwner = viewModelStoreOwner
            ) {
                ArticleModal(
                    author = userId,
                    username = account.username,
                    imageUrl = account.imageUrl,
                    types = types,
                    limit = BuildConfig.PAGING_LIMIT,
                    selected = selected,
                    timestamp = timestamp,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner
                ) {}
            }
        }
    }
}
