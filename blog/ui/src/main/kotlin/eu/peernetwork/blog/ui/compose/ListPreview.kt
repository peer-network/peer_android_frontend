package eu.peernetwork.blog.ui.compose

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
fun ListPreview(
    listState: LazyListState,
    onClear: () -> Unit = {},
    content: @Composable (State<Int>) -> Unit
) {
    var position by remember { mutableIntStateOf(-1) }
    var currentPosition = remember { mutableIntStateOf(-1) }
    val handleClear by rememberUpdatedState(onClear)
    val updatedContent by rememberUpdatedState(content)
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .onEach {
                if (listState.isScrollInProgress) {
                    handleClear()
                }
            }.debounce(800)
            .collectLatest {
                val visibleItems = listState.layoutInfo.visibleItemsInfo
                if (visibleItems.isEmpty()) {
                    position = -1
                    return@collectLatest
                }
                val mostVisible = visibleItems
                    .maxByOrNull { item ->
                        val visibleHeight = (minOf(item.offset + item.size,
                            listState.layoutInfo.viewportEndOffset) - maxOf(item.offset, 0))
                        visibleHeight.toFloat() / item.size
                    }
                val newPosition = when {
                    mostVisible == null -> listState.firstVisibleItemIndex
                    (mostVisible.let { item ->
                        val visibleHeight = (minOf(item.offset + item.size,
                            listState.layoutInfo.viewportEndOffset) - maxOf(item.offset, 0))
                        visibleHeight.toFloat() / item.size > 0.65f
                    }) -> mostVisible.index
                    else -> listState.firstVisibleItemIndex
                }
                if (position != newPosition) {
                    position = newPosition
                }
                if (!listState.isScrollInProgress) {
                    currentPosition.intValue = position
                }
            }
    }
    updatedContent(currentPosition)
}
