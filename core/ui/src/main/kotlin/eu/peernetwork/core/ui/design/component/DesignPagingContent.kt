package eu.peernetwork.core.ui.design.component

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow

@Composable
fun<T : Any> DesignPagingContent(
    state: State<DesignStatefulContentState>,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    durationMillis: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    contentAlignment: Alignment = Alignment.Center,
    placeholder: (@Composable () -> Unit)? = null,
    errorContent: (@Composable (Throwable) -> Unit)? = null,
    content: @Composable (State<DesignStatefulContentState>, LazyPagingItems<T>) -> Unit,
) {
    DesignStatefulContent<Flow<PagingData<T>>>(
        state = state,
        modifier = modifier,
        onRefresh = onRefresh,
        durationMillis = durationMillis,
        easing = easing,
        contentAlignment = contentAlignment,
        placeholder = placeholder,
        errorContent = {
            errorContent?.invoke(it)
                ?: DesignErrorContent(
                    it, onRetry = onRefresh,
                    modifier = Modifier.fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) },
    ) { flow ->
        val lazyPagingItems = flow.collectAsLazyPagingItems()
        val contentState = remember(lazyPagingItems.loadState.refresh) { derivedStateOf {
            if (lazyPagingItems.loadState.refresh is LoadState.Error) {
                DesignStatefulContentState.Error((lazyPagingItems.loadState.refresh as LoadState.Error).error)
            } else if (lazyPagingItems.itemCount > 0) {
                DesignStatefulContentState.Success(lazyPagingItems)
            } else if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                DesignStatefulContentState.Loading
            } else if (state.value is DesignStatefulContentState.Success<*>) {
                DesignStatefulContentState.Success(lazyPagingItems)
            } else {
                state.value
            }
        } }
        DesignStatefulContent<LazyPagingItems<T>>(
            state = contentState,
            onRefresh = onRefresh,
            autoRefresh = false,
            durationMillis = durationMillis,
            easing = easing,
            contentAlignment = contentAlignment,
            placeholder = placeholder,
            errorContent = {
                errorContent?.invoke(it)
                    ?: DesignErrorContent(
                        it, onRetry = onRefresh,
                        modifier = Modifier.fillMaxSize()
                            .padding(bottom = 64.dp)
                            .verticalScroll(rememberScrollState())
                    ) },
            content = { content(contentState, it) }
        )
    }
}
