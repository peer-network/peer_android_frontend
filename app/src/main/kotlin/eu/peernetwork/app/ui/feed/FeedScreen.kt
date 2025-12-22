package eu.peernetwork.app.ui.feed

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.ads.ui.article.ArticleNavigator
import eu.peernetwork.ads.ui.boost.BoostModal
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.social.ui.connection.ConnectionScreen
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.app.interactor.NavigationInteractor

@Composable
fun FeedScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Feed.Component, FeedViewModel) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Feed.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = FeedViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val updatedContent by rememberUpdatedState(content)
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { updatedContent(component, viewModel) }
}

@Composable
fun FeedScreen(
    account: Account,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    refresh: MutableState<Boolean>,
    title: String? = null,
    criteria: Criteria = Criteria.None,
    onExplore: () -> Unit
) {
    val context = LocalContext.current
    val controller = rememberNavController()
    val selected = remember { mutableIntStateOf(-1) }
    val navigator = remember { NavigationInteractor(context, controller) }
    CompositionLocalProvider(
        PostNavigator.LocalPostNavigator provides navigator,
        ArticleNavigator.LocalArticleNavigator provides navigator,
    ) {
        FeedScreen(
            provider = provider,
            viewModelStoreOwner = viewModelStoreOwner,
        ) { component, viewModel ->
            val state by viewModel.state.collectAsStateWithLifecycle()
            val isVisible = remember { mutableStateOf(false) }
            val showBoost = remember { mutableStateOf<String?>(null) }
            val ordinal = remember {
                derivedStateOf {
                    (state as? FeedViewModel.State.Initialize?)?.filter ?: 0
                }
            }
            val pageState = rememberPagerState(
                pageCount = { UiMimeType.TYPES.size },
                initialPage = state.page
            )
            FeedNavigation(
                account = account,
                component = component,
                controller = controller,
            ) {
                FeedPage(
                    uuid = account.id,
                    username = account.username,
                    imageUrl = account.imageUrl,
                    title = title,
                    isVisible = isVisible,
                    selected = selected,
                    limit = limit,
                    ordinal = ordinal.value,
                    criteria = criteria,
                    refresh = refresh,
                    pageState = pageState,
                    component = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    onExplore = onExplore,
                    onFilter = { viewModel.setFilter(it) },
                    onBoost = { showBoost.value = it }
                ) { isVisible.value = true }
                DisposableEffect(Unit) {
                    onDispose {
                        viewModel.lastVisited(pageState.currentPage)
                    }
                }
            }
            FeedModal(
                account = account,
                selected = selected,
                isVisible = isVisible,
                criteria = criteria,
                category = if (pageState.currentPage == 0) {
                    Category.FOLLOWED
                } else {
                    Category.FOLLOWER
                },
                provider = provider,
                viewModelStoreOwner = viewModelStoreOwner
            )
            BoostModal(
                state = showBoost
            ) { controller.navigate("boost/$it") }
        }
    }
}
