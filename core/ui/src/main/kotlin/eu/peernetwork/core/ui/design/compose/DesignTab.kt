package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DesignTab(
    state: PagerState,
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.tertiaryContainer,
    foreground: Color = MaterialTheme.colorScheme.tertiary,
    shape: Shape = RoundedCornerShape(2.dp),
    content: @Composable (Int) -> Unit
) {
    val coroutine = rememberCoroutineScope()
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(state.pageCount) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f)
                        .graphicsLayer {
                            alpha = if (it == state.currentPage) 1f else .6f
                        }
                        .clickable(role = Role.Button) {
                            coroutine.launch {
                                state.animateScrollToPage(it)
                            }
                        }
                ) { content(it) }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(background)
        ) {
            val indicatorWidth = with(LocalDensity.current) {
                (LocalConfiguration.current.screenWidthDp.dp / state.pageCount).toPx()
            }
            val offsetX by remember {
                derivedStateOf {
                    (state.currentPage + state.currentPageOffsetFraction) * indicatorWidth
                }
            }
            Box(
                modifier = Modifier
                    .offset { IntOffset(offsetX.roundToInt(), 0) }
                    .width(with(LocalDensity.current) { indicatorWidth.toDp() })
                    .fillMaxHeight()
                    .background(foreground, shape = shape)
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignTab() {
    PeerTheme {
        DesignTab(
            rememberPagerState { 3 }
        ) {
            Text("$it")
        }
    }
}
