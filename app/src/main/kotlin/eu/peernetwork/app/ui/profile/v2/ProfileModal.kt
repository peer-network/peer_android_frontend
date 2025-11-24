package eu.peernetwork.app.ui.profile.v2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.blog.domain.usecase.PostUsecase
import eu.peernetwork.blog.ui.article.ArticleOverlay
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignOverlay

@Composable
fun ProfileModal(
    principal: String,
    userId: String,
    isVisible: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val controller = rememberNavController()
    DesignOverlay(
        state = isVisible,
        onDismiss = { isVisible.value = false }
    ) {
        ProfileScreen(
            provider = provider,
            viewModelStoreOwner = viewModelStoreOwner
        ) { component, connection ->
            ProfileNavigation(
                principal = principal,
                userId = userId,
                controller = controller,
                provider = provider,
                component = component,
                viewModelStoreOwner = viewModelStoreOwner
            ) {
                ArticleOverlay(
                    author = userId,
                    types = PostUsecase.POST,
                    enabled = isVisible.value,
                    limit = 20,
                    position = 1,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    event = object : UiPostListener {
                        override fun invoke(event: UiPostListener.Event) {
                        }
                    },
                    header = {

                    }
                ) {}
            }
        }
    }
}
