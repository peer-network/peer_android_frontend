package eu.peernetwork.core.ui.design.luna

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun DesignBox(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    background: @Composable BoxScope.() -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    val updatedBackground by rememberUpdatedState(background)
    val updateContent by rememberUpdatedState(content)
    Layout(
        modifier = modifier,
        content = {
            Box(
                contentAlignment = contentAlignment,
                propagateMinConstraints = propagateMinConstraints
            ) { updatedBackground() }
            Box(
                contentAlignment = contentAlignment,
                propagateMinConstraints = propagateMinConstraints
            ) { updateContent() }
        }
    ) { measurables: List<Measurable>, constraints: Constraints ->
        require(measurables.size == 2) { "BackgroundMatchingLayout expects exactly two children" }
        val foregroundMeasurable = measurables[1]
        val foregroundPlaceable: Placeable = foregroundMeasurable.measure(constraints)
        val backgroundMeasurable = measurables[0]
        val backgroundPlaceable: Placeable = backgroundMeasurable.measure(
            Constraints.fixed(
                width = foregroundPlaceable.width,
                height = foregroundPlaceable.height
            )
        )
        layout(
            width = foregroundPlaceable.width,
            height = foregroundPlaceable.height
        ) {
            backgroundPlaceable.placeRelative(x = 0, y = 0)
            foregroundPlaceable.placeRelative(x = 0, y = 0)
        }
    }
}

@Preview
@Composable
fun DesignBoxPreview() {
    DesignTheme {
        DesignBox(background = {
            Box(modifier = Modifier.fillMaxSize()
                .padding(2.dp)
                .background(MaterialTheme.colorScheme.primary)
                .align(Alignment.Center))
        }) {
            Box(modifier = Modifier.size(24.dp))
        }
    }
}
