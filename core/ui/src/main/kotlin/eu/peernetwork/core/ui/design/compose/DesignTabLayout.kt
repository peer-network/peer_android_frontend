package eu.peernetwork.core.ui.design.compose

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun DesignTabLayout(
    state: PagerState,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.onTertiaryContainer,
    fitEvenly: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(bottom = 8.dp),
    indicator: @Composable () -> Unit = {},
    content: @Composable (Int) -> Unit
) {
    BoxWithConstraints(modifier = modifier.drawBehind {
        val width = strokeWidth.toPx()
        val y = size.height - width / 2
        drawLine(
            color = color,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = width
        )
    }) {
        val availableWidth = maxWidth
        val updatedIndicator by rememberUpdatedState(indicator)
        val updatedContent by rememberUpdatedState(content)
        val availableWidthPx = with(LocalDensity.current) { availableWidth.toPx().toInt() }
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            Layout(
                modifier = Modifier.padding(contentPadding),
                content = {
                    repeat(state.pageCount) {
                        updatedContent(it)
                    }
                    Box(Modifier.layoutId("indicator")) {
                        updatedIndicator()
                    }
                }
            ) { measurables, constraints ->
                val tabMeasurables = measurables.filterNot { it.layoutId == "indicator" }
                val indicatorMeasurable = measurables.firstOrNull { it.layoutId == "indicator" }
                val placeables = if (fitEvenly && state.pageCount > 0) {
                    val itemWidth = availableWidthPx / state.pageCount
                    tabMeasurables.map { measurable ->
                        measurable.measure(
                            Constraints.fixedWidth(itemWidth).copy(
                                minHeight = constraints.minHeight,
                                maxHeight = constraints.maxHeight
                            )
                        )
                    }
                } else {
                    tabMeasurables.map { measurable -> measurable.measure(constraints) }
                }
                val rowHeight = placeables.maxOfOrNull { it.height } ?: 100
                val rowWidth = placeables.sumOf { it.width }
                val maxLayoutWidth = maxOf(constraints.maxWidth, availableWidthPx)
                val layoutWidth = minOf(rowWidth, maxLayoutWidth)
                val layoutHeight = rowHeight
                layout(layoutWidth, layoutHeight) {
                    var xPosition = 0
                    placeables.forEach { placeable ->
                        if (xPosition + placeable.width <= layoutWidth) {
                            placeable.placeRelative(x = xPosition, y = 0)
                        } else {
                            placeable.placeRelative(x = xPosition, y = 0)
                        }
                        xPosition += placeable.width
                    }
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignTabLayout() {
    PeerTheme {
        val tabs = listOf("Home", "Explore", "Profile", "Settings", "More")
        val state = rememberPagerState { tabs.size }
        Column(modifier = Modifier.fillMaxSize()) {
            DesignTabLayout(
                state = state,
                fitEvenly = false,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = tabs[it],
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
    }
}
