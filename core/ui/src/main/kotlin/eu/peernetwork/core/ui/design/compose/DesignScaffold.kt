package eu.peernetwork.core.ui.design.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import kotlinx.coroutines.launch

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun DesignScaffold(
    modifier: Modifier = Modifier,
    alwaysReturn: Boolean = false,
    header: @Composable (State<Float>) -> Unit = {},
    footer: @Composable (State<Float>) -> Unit = {},
    overlay: @Composable (State<Float>) -> Unit = {},
    content: @Composable (State<Float>) -> Unit
) {
    val maxOffset = 1f
    val minOffset = 0f
    val scroll = rememberScrollState()
    val coroutine = rememberCoroutineScope()
    var normalizedOffset = remember { mutableFloatStateOf(0f) }
    val updatedHeader by rememberUpdatedState(header)
    val updatedOverlay by rememberUpdatedState(overlay)
    val updatedFooter by rememberUpdatedState(footer)
    val updatedContent by rememberUpdatedState(content)
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.x != 0f) return Offset.Zero
                if (normalizedOffset.floatValue <= minOffset && available.y > 0) {
                    return Offset.Zero
                }
                val headerDelta = available.y / scroll.maxValue
                val offset = (normalizedOffset.floatValue - headerDelta)
                normalizedOffset.floatValue = offset.coerceIn(minOffset, maxOffset)
                val canReturn = alwaysReturn && normalizedOffset.floatValue < 1
                return if (canReturn || scroll.value < scroll.maxValue) {
                    coroutine.launch { scroll.scrollBy(-available.y) }
                    available
                } else {
                    Offset.Zero
                }
            }
        }
    }
    BoxWithConstraints(modifier = modifier.fillMaxSize()
        .nestedScroll(nestedScrollConnection)) {
        val height = maxHeight
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(scroll)
        ) {
            updatedHeader(normalizedOffset)
            Column(modifier = Modifier.fillMaxWidth()
                .height(height)) {
                updatedContent(normalizedOffset)
            }
        }
        Box { updatedOverlay(normalizedOffset) }
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.align(Alignment.BottomCenter)
                .clipToBounds()
        ) {
            Box(modifier = Modifier.graphicsLayer {
                translationY = normalizedOffset.floatValue * size.height
            }) { updatedFooter(normalizedOffset) }
        }
    }
}
