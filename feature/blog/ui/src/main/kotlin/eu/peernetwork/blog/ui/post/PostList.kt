package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach

@Composable
@OptIn(FlowPreview::class)
fun PostList(
    listState: LazyListState,
    onClear: () -> Unit = {},
    onFocused: (Int) -> Unit = {},
    onFocus: (Int) -> Unit = {},
    content: @Composable (State<Int>) -> Unit
) {
    var position by remember { mutableIntStateOf(-1) }
    val currentPosition = remember { mutableIntStateOf(-1) }
    val handleClear by rememberUpdatedState(onClear)
    val handleFocus by rememberUpdatedState(onFocus)
    val handleOnFocused by rememberUpdatedState(onFocused)
    val updatedContent by rememberUpdatedState(content)
    LaunchedEffect(Unit) {
        if (listState.firstVisibleItemIndex == 0) {
            currentPosition.intValue = 0
        }
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .onEach {
                if (listState.isScrollInProgress) {
                    handleClear()
                } else if (position != -1) {
                    handleFocus(position)
                }
            }.debounce(500)
            .collectLatest {
                val visibleItems = listState.layoutInfo.visibleItemsInfo
                if (visibleItems.isEmpty()) {
                    position = -1
                    return@collectLatest
                }
                val mostVisible = visibleItems
                    .maxByOrNull { item ->
                        val visibleHeight = (minOf(item.offset + item.size,
                            listState.layoutInfo.viewportEndOffset
                        ) - maxOf(item.offset, 0))
                        visibleHeight.toFloat() / item.size
                    }
                val newPosition = when {
                    mostVisible == null -> listState.firstVisibleItemIndex
                    (mostVisible.let { item ->
                        val visibleHeight = (minOf(item.offset + item.size,
                            listState.layoutInfo.viewportEndOffset
                        ) - maxOf(item.offset, 0))
                        visibleHeight.toFloat() / item.size > 0.65f
                    }) -> mostVisible.index
                    else -> listState.firstVisibleItemIndex
                }
                if (position != newPosition) {
                    position = newPosition
                }
                if (!listState.isScrollInProgress) {
                    handleOnFocused(position)
                    currentPosition.intValue = position
                }
            }
    }
    updatedContent(currentPosition)
}
