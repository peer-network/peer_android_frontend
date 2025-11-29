package eu.peernetwork.app.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.blog.domain.usecase.PostUsecase
import eu.peernetwork.blog.ui.article.ArticleModal
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignOverlay

@Composable
fun ProfileModal(
    principal: String,
    userId: String,
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
                principal = principal,
                userId = userId,
                controller = controller,
                provider = provider,
                component = component,
                viewModelStoreOwner = viewModelStoreOwner
            ) {
                ArticleModal(
                    author = userId,
                    types = PostUsecase.POST,
                    limit = 20,
                    selected = selected,
                    timestamp = timestamp,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner
                ) {}
            }
        }
    }
}
