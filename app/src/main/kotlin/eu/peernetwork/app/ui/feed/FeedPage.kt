package eu.peernetwork.app.ui.feed

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.app.mapper.mapToCriteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.ui.model.UiFilter
import eu.peernetwork.blog.ui.timeline.TimelineEvent
import eu.peernetwork.blog.ui.timeline.TimelineList
import kotlinx.coroutines.launch

@Composable
fun FeedPage(
    id: String,
    username: String,
    imageUrl: String,
    ordinal: Int,
    limit: Int,
    title: String?,
    criteria: Criteria?,
    isVisible: State<Boolean>,
    selected: MutableIntState,
    pageState: PagerState,
    component: Feed.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    left: LazyListState = rememberLazyListState(),
    right: LazyListState = rememberLazyListState(),
    onFilter: (Int) -> Unit,
    onClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val filter = remember(criteria) { mutableStateOf(criteria) }
    val derivedCriteria = remember(ordinal, filter.value) { derivedStateOf {
        val content = criteria as? Criteria.Content?
        filter.value ?: UiFilter.entries.getOrNull(ordinal)?.mapToCriteria(
            tag = content?.tag,
            title = content?.title
        ) ?: Criteria.None
    } }
    val handleClick by rememberUpdatedState(onClick)
    val handleOnFilter by rememberUpdatedState(onFilter)
    FeedScaffold(pageState = pageState) {
        TimelineList(
            id = id,
            username = username,
            imageUrl = imageUrl,
            status = isVisible,
            selected = selected,
            limit = limit,
            category = if (it == 0) {
                Category.FOLLOWED
            } else {
                Category.FOLLOWER
            },
            criteria = derivedCriteria.value,
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner,
            onEvent = { event ->
                when(event) {
                    is TimelineEvent.Post -> handleClick()
                }
            },
            listState = if (it == 0) {
                left
            } else {
                right
            }
        ) {}
    }
    FeedMenu(
        id = id,
        default = ordinal,
        title = title,
        onSelect = { ordinal, criteria ->
            handleOnFilter(ordinal)
            filter.value = criteria
        }
    ) {
        scope.launch {
            if (pageState.currentPage == 0) {
                left.animateScrollToItem(0)
            } else {
                right.animateScrollToItem(0)
            }
        }
    }
}
