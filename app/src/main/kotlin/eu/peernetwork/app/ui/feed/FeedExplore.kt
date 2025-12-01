package eu.peernetwork.app.ui.feed

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Sort
import eu.peernetwork.blog.ui.timeline.TimelineList
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.media.core.model.UiMimeType
import kotlinx.coroutines.launch

@Composable
fun FeedExplore(
    id: String,
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
            id = id,
            component = component,
            controller = navHostController,
            viewModelStoreOwner = viewModelStoreOwner,
        ) { updatedContent(component, viewModel) }
    }
}

@Composable
fun FeedExplore(
    id: String,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    refresh: MutableState<Boolean>,
    title: String? = null,
    criteria: Criteria? = null,
) {
    val left = rememberLazyListState()
    val right = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val controller = rememberNavController()
    val selected = remember { mutableIntStateOf(-1) }
    FeedExplore(
        id = id,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
        navHostController = controller
    ) { component, viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        val position = remember { mutableIntStateOf(state.page) }
        val pageState = rememberPagerState(
            pageCount = { UiMimeType.TYPES.size },
            initialPage = position.intValue
        )
        val sorts = listOf(Sort.NEW, Sort.TREND)
        val derivedCriteria = remember { derivedStateOf {
            (criteria as? Criteria.Content?)?.copy(
                sort = sorts[pageState.currentPage],
            ) ?: Criteria.Content(sort = sorts[pageState.currentPage])
        } }
        val enable = remember { mutableStateOf(false) }
        FeedExploreScaffold(pageState = pageState) {
            TimelineList(
                id = id,
                status = enable,
                selected = selected,
                limit = limit,
                category = Category.NONE,
                criteria = derivedCriteria.value,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                refresh = refresh,
                listState = if (it == 0) {
                    left
                } else {
                    right
                }
            ) {}
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
