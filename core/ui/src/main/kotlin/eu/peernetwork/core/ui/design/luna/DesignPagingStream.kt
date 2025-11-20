package eu.peernetwork.core.ui.design.luna

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
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
import kotlinx.coroutines.flow.Flow

@Composable
fun<T : Any> DesignPagingStream(
    state: State<DesignStreamState<Flow<PagingData<T>>>>,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    label: String = "Crossfade",
    durationMillis: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    default: @Composable () -> Unit = {},
    loading: @Composable () -> Unit = {},
    error: @Composable (error: State<Throwable>) -> Unit = {},
    content: @Composable (data: LazyPagingItems<T>) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignStream(
        state = state,
        modifier = modifier,
        animationSpec = animationSpec,
        label = label,
        durationMillis = durationMillis,
        easing = easing,
        default = default,
        loading = loading,
        error = error
    ) {
        val lazyPagingItems = it.value.collectAsLazyPagingItems()
        val derivedState = remember { derivedStateOf {
            if (lazyPagingItems.itemCount > 0) {
                DesignStreamState.Success(lazyPagingItems)
            } else if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                DesignStreamState.Loading
            } else if (lazyPagingItems.loadState.refresh is LoadState.Error) {
                val error = (lazyPagingItems.loadState.refresh as LoadState.Error).error
                DesignStreamState.Error(error)
            } else {
                DesignStreamState.Default
            }
        } }
        DesignStream(
            state = derivedState,
            modifier = modifier,
            animationSpec = animationSpec,
            label = label,
            durationMillis = durationMillis,
            easing = easing,
            default = default,
            loading = loading,
            error = error
        ) { items -> updatedContent(items.value) }
    }
}
