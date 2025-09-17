package eu.peernetwork.core.ui.design.compose

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.materii.pullrefresh.DragRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState
import eu.peernetwork.core.ui.exception.NoContentException
import kotlinx.coroutines.flow.Flow

sealed interface DesignPagerState {
    data object Default : DesignPagerState
    data object Loading : DesignPagerState
    data class Error(val error: Throwable) : DesignPagerState
}

@Composable
fun<T : Any> DesignPager(
    state: State<DesignSceneState<Flow<PagingData<T>>>>,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    label: String = "DesignPager",
    default: @Composable () -> Unit = {},
    loading: @Composable () -> Unit = {},
    error: @Composable (error: State<Throwable>) -> Unit = {},
    content: @Composable (State<DesignPagerState>, data: State<LazyPagingItems<T>>) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignScene(
        state = state,
        modifier = modifier,
        animationSpec = animationSpec,
        label = label,
        default = default,
        loading = loading,
        error = error
    ) {
        val lazyPagingItems = it.value.collectAsLazyPagingItems()
        val listState = remember { derivedStateOf { lazyPagingItems } }
        val pageState = remember { derivedStateOf {
            when (lazyPagingItems.loadState.refresh) {
                is LoadState.Error -> DesignPagerState.Error(
                    (lazyPagingItems.loadState.refresh as LoadState.Error).error)
                is LoadState.Loading -> DesignPagerState.Loading
                else -> DesignPagerState.Default
            }
        } }
        updatedContent(pageState, listState)
    }
}

@Composable
fun<T : Any> DesignRefreshablePager(
    state: State<DesignSceneState<Flow<PagingData<T>>>>,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    label: String = "DesignRefreshablePager",
    enable: Boolean = true,
    onRefresh: () -> Unit = {},
    default: @Composable () -> Unit = {},
    loading: @Composable () -> Unit = {},
    empty: @Composable () -> Unit = {},
    error: @Composable (error: State<Throwable>) -> Unit = {},
    content: @Composable (State<DesignPagerState>, data: State<LazyPagingItems<T>>) -> Unit,
) {
    val updatedContent by rememberUpdatedState(content)
    DesignPager(
        state = state,
        modifier = modifier,
        animationSpec = animationSpec,
        label = label,
        default = default,
        loading = loading,
        error = error
    ) { pageState, lazyPagingItems ->
        val handleRefresh by rememberUpdatedState(onRefresh)
        val isLoading = remember { derivedStateOf {
            lazyPagingItems.value.itemCount > 0
                    && lazyPagingItems.value.loadState.refresh is LoadState.Loading
        } }
        val derivedState = remember { derivedStateOf {
            if (lazyPagingItems.value.itemCount > 0) {
                DesignSceneState.Success(lazyPagingItems.value)
            } else {
                when (lazyPagingItems.value.loadState.refresh) {
                    is LoadState.Error -> DesignSceneState.Error(
                        error = (lazyPagingItems.value.loadState.refresh as LoadState.Error).error
                    )
                    is LoadState.Loading -> DesignSceneState.Loading
                    else -> DesignSceneState.Success(lazyPagingItems.value)
                }
            }
        } }
        val updatedLoading by rememberUpdatedState(loading)
        val updatedEmpty by rememberUpdatedState(empty)
        val updatedError by rememberUpdatedState(error)
        Crossfade(derivedState.value) { target ->
            when (target) {
                is DesignSceneState.Loading -> DesignLoader { updatedLoading() }
                is DesignSceneState.Error -> {
                    if (target.error is NoContentException) {
                        updatedEmpty()
                    } else {
                        updatedError(remember { derivedStateOf { target.error } })
                    }
                }
                else -> if (enable) {
                    val refreshState = rememberPullRefreshState(
                        refreshing = isLoading.value,
                        onRefresh = {
                            if (lazyPagingItems.value.itemCount > 0) {
                                lazyPagingItems.value.refresh()
                            } else {
                                handleRefresh()
                            }
                        }
                    )
                    DragRefreshLayout(state = refreshState) {
                        updatedContent(pageState, lazyPagingItems)
                    }
                } else {
                    updatedContent(pageState, lazyPagingItems)
                }
            }
        }
    }
}
