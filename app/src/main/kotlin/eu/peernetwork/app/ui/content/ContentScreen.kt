package eu.peernetwork.app.ui.content

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.interactor.NavigationInteractor
import eu.peernetwork.blog.ui.detail.DetailScreen
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.connection.ConnectionScreen
import eu.peernetwork.user.domain.model.Account

@Composable
fun ContentScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Content.Component) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Content.Builder::class.java).build(context)
    }
    val updatedContent by rememberUpdatedState(content)
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { updatedContent(component) }
}

@Composable
fun ContentScreen(
    account: Account,
    postId: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val controller = rememberNavController()
    val overlay = remember { mutableStateOf<String?>(null) }
    val enable = remember { derivedStateOf { overlay.value == null } }
    val selected = remember { mutableIntStateOf(-1) }
    val navigator = remember { NavigationInteractor(context, controller) }
    CompositionLocalProvider(
        PostNavigator.LocalPostNavigator provides navigator
    ) {
        ContentScreen(
            provider = provider,
            viewModelStoreOwner = viewModelStoreOwner
        ) { component ->
            ContentNavigation(
                account = account,
                postLimit = BuildConfig.PAGING_LIMIT,
                component = component,
                controller = controller
            ) {
                DetailScreen(
                    uuid = account.id,
                    username = account.username,
                    imageUrl = account.imageUrl,
                    limit = BuildConfig.PAGING_LIMIT,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                ) { component, viewModel ->
                    DetailScreen(
                        id = postId,
                        uuid = account.id,
                        enable = enable,
                        selected = selected,
                        component = component,
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
    DesignTitleBarHost("ContentScreen$postId") {
        titleBar {
            DesignTitle {
                Text(stringResource(eu.peernetwork.blog.ui.R.string.post_label))
            }
        }
    }
}
