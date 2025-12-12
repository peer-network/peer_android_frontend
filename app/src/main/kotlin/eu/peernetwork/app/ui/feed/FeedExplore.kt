package eu.peernetwork.app.ui.feed

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.interactor.NavigationInteractor
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Sort
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.blog.ui.timeline.TimelineList
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.user.domain.model.Account
import kotlinx.coroutines.launch

@Composable
fun FeedExplore(
    account: Account,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    navHostController: NavHostController,
    content: @Composable (Feed.Component, FeedViewModel) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    FeedScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
    ) { component, viewModel ->
        FeedNavigation(
            account = account,
            component = component,
            controller = navHostController,
        ) { updatedContent(component, viewModel) }
    }
}

@Composable
fun FeedExplore(
    account: Account,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    refresh: MutableState<Boolean> = remember { mutableStateOf(false) },
    title: String? = null,
    criteria: Criteria = Criteria.None,
) {
    val context = LocalContext.current
    val left = rememberLazyListState()
    val right = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val controller = rememberNavController()
    val isVisible = remember { mutableStateOf(false) }
    val selected = remember { mutableIntStateOf(-1) }
    val navigator = remember { NavigationInteractor(context, controller) }
    CompositionLocalProvider(
        PostNavigator.LocalPostNavigator provides navigator
    ) {
        FeedExplore(
            account = account,
            provider = provider,
            viewModelStoreOwner = viewModelStoreOwner,
            navHostController = controller
        ) { component, viewModel ->
            val state by viewModel.state.collectAsStateWithLifecycle()
            val position = remember { mutableIntStateOf(state.page) }
            val sorts = listOf(Sort.NEW, Sort.TREND)
            val pageState = rememberPagerState(
                pageCount = { sorts.size },
                initialPage = position.intValue
            )
            val derivedCriteria = remember { derivedStateOf {
                (criteria as? Criteria.Content?)?.copy(
                    sort = sorts[pageState.currentPage],
                ) ?: Criteria.Content(sort = sorts[pageState.currentPage])
            } }
            FeedExploreScaffold(pageState = pageState) {
                TimelineList(
                    id = account.id,
                    username = account.username,
                    imageUrl = account.imageUrl,
                    status = isVisible,
                    selected = selected,
                    refresh = refresh,
                    limit = limit,
                    category = Category.NONE,
                    criteria = derivedCriteria.value,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    listState = if (it == 0) {
                        left
                    } else {
                        right
                    },
                    onEvent = { isVisible.value = true },
                    onExplore = {  }
                )
                FeedModal(
                    account = account,
                    selected = selected,
                    isVisible = isVisible,
                    criteria = derivedCriteria.value,
                    category = Category.NONE,
                    provider = provider,
                    viewModelStoreOwner = viewModelStoreOwner
                )
            }
            DesignTitleBarHost(
                tag = "FeedExplorer",
                listener = {
                    scope.launch {
                        if (pageState.currentPage == 0) {
                            left.animateScrollToItem(0)
                        } else {
                            right.animateScrollToItem(0)
                        }
                    }
                }
            ) {
                titleBar {
                    DesignTitle {
                        Text(title ?: stringResource(R.string.explore_label))
                    }
                }
            }
        }
    }
}
