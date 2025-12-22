package eu.peernetwork.app.ui.search

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.ads.ui.article.ArticleNavigator
import eu.peernetwork.app.interactor.NavigationInteractor
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.connection.ConnectionScreen
import eu.peernetwork.user.domain.model.Account
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Search.Component) -> Unit
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Search.Builder::class.java).build(context) }
    val updatedContent by rememberUpdatedState(content)
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { updatedContent(component) }
}

@Composable
fun SearchScreen(
    account: Account,
    limit: Int,
    mode: SearchMode,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    title: String? = null,
    query: String? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val controller = rememberNavController()
    val currentMode = remember { mutableStateOf(mode) }
    val listState = rememberLazyGridState()
    val selected = remember { mutableIntStateOf(-1) }
    val isVisible = remember { mutableStateOf(false) }
    val navigator = remember { NavigationInteractor(context, controller) }
    val state by rememberSaveable(stateSaver = TextFieldState.Saver) {
        mutableStateOf(TextFieldState(query ?: ""))
    }
    CompositionLocalProvider(
        PostNavigator.LocalPostNavigator provides navigator,
        ArticleNavigator.LocalArticleNavigator provides navigator,
    ) {
        SearchScreen(
            provider = provider,
            viewModelStoreOwner = viewModelStoreOwner,
        ) { component ->
            SearchNavigation(
                account = account,
                controller = controller,
                component = component
            ) {
                SearchPage(
                    state,
                    currentMode
                ) {
                    SearchSuggestion(
                        state = state,
                        selected = selected,
                        mode = currentMode.value,
                        limit = limit,
                        onClick = {
                            when (currentMode.value) {
                                is SearchMode.Tag -> controller.navigate("feed/tag/$it")
                                is SearchMode.Title -> controller.navigate("feed/title/$it")
                                else -> controller.navigate("profile/$it")
                            }
                            false
                        },
                        onShow = { isVisible.value = true },
                        component = component,
                        viewModelStoreOwner = viewModelStoreOwner,
                        listState = listState,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                }
                DesignTitleBarHost(
                    tag = "SearchScreen",
                    listener = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }
                ) {
                    titleBar {
                        DesignTitle {
                            Text(title ?: stringResource(R.string.search_label))
                        }
                    }
                }
            }
            SearchModal(
                account = account,
                selected = selected,
                isVisible = isVisible,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            )
        }
    }
}
