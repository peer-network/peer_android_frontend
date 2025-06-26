package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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

@Composable
@OptIn(FlowPreview::class)
fun ListPreview(
    listState: LazyListState,
    content: @Composable (Int) -> Unit
) {
    var position by remember { mutableIntStateOf(-1) }
    var currentPosition by remember { mutableIntStateOf(-1) }
    val updatedContent by rememberUpdatedState(content)
    val layoutInfo by remember { derivedStateOf { listState.layoutInfo } }
    LaunchedEffect(layoutInfo) {
        val visibleItems = listState.layoutInfo.visibleItemsInfo
        if (visibleItems.isEmpty()) {
            position = -1
            return@LaunchedEffect
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
                visibleHeight.toFloat() / item.size > 0.4f
            }) -> mostVisible.index
            else -> listState.firstVisibleItemIndex
        }
        if (position != newPosition) {
            position = newPosition
        }
    }
    LaunchedEffect(layoutInfo) {
        snapshotFlow { layoutInfo }
            .distinctUntilChanged()
            .debounce(500)
            .collectLatest { currentPosition = position }
    }
    updatedContent(currentPosition)
}
