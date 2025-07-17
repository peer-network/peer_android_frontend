package eu.peernetwork.core.ui.design.compose

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlin.math.roundToInt

enum class DesignBottomSheetScaffoldState { EXPAND, HIDE }

@Composable
fun DesignBottomSheetScaffold(
    state: State<Boolean>,
    behind: Boolean = true,
    color: Color = MaterialTheme.colorScheme.tertiaryContainer,
    orientation: Orientation = Orientation.Vertical,
    confirmValueChange: (DesignBottomSheetScaffoldState) -> Boolean = { true },
    onStateChanged: (DesignBottomSheetScaffoldState) -> Unit = {},
    onDismiss: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val visible = remember { mutableStateOf(false) }
    val updatedContent by rememberUpdatedState(content)
    val handleOnDismiss by rememberUpdatedState(onDismiss)
    val dialogState = remember { mutableStateOf(DesignBottomSheetScaffoldState.HIDE) }
    DesignDialog(
        state = visible,
        behind = behind,
        onDismiss = onDismiss,
        canDismiss = dialogState.value == DesignBottomSheetScaffoldState.HIDE,
        onBackPressed = { dialogState.value = DesignBottomSheetScaffoldState.HIDE }
    ) { controller, anim, cancelable ->
        val isDismissed = remember(anim.value, visible.value) {
            derivedStateOf { !visible.value && anim.value == 0f }
        }
        DesignBottomSheetScaffold(
            state = dialogState.value,
            color = color,
            orientation = orientation,
            confirmValueChange = confirmValueChange,
            onStateChanged = onStateChanged,
            onDismiss = {
                visible.value = false
                dialogState.value = DesignBottomSheetScaffoldState.HIDE
                handleOnDismiss()
            }
        ) { updatedContent() }
        LaunchedEffect(isDismissed.value) {
            if (isDismissed.value) {
                handleOnDismiss()
            }
        }
        LaunchedEffect(state.value) {
            if (state.value) {
                dialogState.value = DesignBottomSheetScaffoldState.EXPAND
            }
        }
    }
    LaunchedEffect(state.value) {
        if (state.value) {
            visible.value = true
        } else {
            dialogState.value = DesignBottomSheetScaffoldState.HIDE
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun DesignBottomSheetScaffold(
    state: DesignBottomSheetScaffoldState,
    color: Color = MaterialTheme.colorScheme.tertiaryContainer,
    shape: Shape = ShapeDefaults.ExtraLarge.copy(
        bottomStart = CornerSize(0.0.dp),
        bottomEnd = CornerSize(0.0.dp)
    ),
    orientation: Orientation = Orientation.Vertical,
    confirmValueChange: (DesignBottomSheetScaffoldState) -> Boolean = { true },
    onStateChanged: (DesignBottomSheetScaffoldState) -> Unit = {},
    onDismiss: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val updatedContent by rememberUpdatedState(content)
    val handleStateChanged by rememberUpdatedState(onStateChanged)
    val handleDismiss by rememberUpdatedState(onDismiss)
    val settledState = remember { mutableStateOf(state) }
    val draggableState = remember {
        AnchoredDraggableState(
            initialValue = state,
            positionalThreshold = { with(density) { 56.dp.toPx() } },
            velocityThreshold = { with(density) { 125.dp.toPx() } },
            snapAnimationSpec = spring<Float>(),
            decayAnimationSpec = exponentialDecay<Float>(),
            confirmValueChange = confirmValueChange,
        )
    }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .draggableAnchors(draggableState, orientation) { sheetSize, constraints ->
                    val layoutHeight = constraints.maxHeight.toFloat()
                    DraggableAnchors {
                        DesignBottomSheetScaffoldState.EXPAND at 0f
                        DesignBottomSheetScaffoldState.HIDE at layoutHeight
                    } to state
                }.anchoredDraggable(
                    state = draggableState,
                    orientation = orientation
                )
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(shape)
                .background(color)
        ) { updatedContent() }
    }
    LaunchedEffect(state) {
        if (state != draggableState.currentValue) {
            draggableState.animateTo(state)
        }
    }
    LaunchedEffect(draggableState.isAnimationRunning) {
        if (settledState.value != draggableState.currentValue) {
            settledState.value = draggableState.currentValue
            handleStateChanged(draggableState.currentValue)
        }
    }
    DisposableEffect(draggableState.currentValue) {
        onDispose {
            if (draggableState.currentValue == DesignBottomSheetScaffoldState.HIDE) {
                handleDismiss()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun <T> Modifier.draggableAnchors(
    state: AnchoredDraggableState<T>,
    orientation: Orientation,
    anchors: (size: IntSize, constraints: Constraints) -> Pair<DraggableAnchors<T>, T>,
) = this then DraggableAnchorsElement(state, anchors, orientation)

@OptIn(ExperimentalFoundationApi::class)
private class DraggableAnchorsElement<T>(
    private val state: AnchoredDraggableState<T>,
    private val anchors: (size: IntSize, constraints: Constraints) -> Pair<DraggableAnchors<T>, T>,
    private val orientation: Orientation
) : ModifierNodeElement<DraggableAnchorsNode<T>>() {

    override fun create() = DraggableAnchorsNode(state, anchors, orientation)

    override fun update(node: DraggableAnchorsNode<T>) {
        node.state = state
        node.anchors = anchors
        node.orientation = orientation
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is DraggableAnchorsElement<*>) return false

        if (state != other.state) return false
        if (anchors !== other.anchors) return false
        if (orientation != other.orientation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = state.hashCode()
        result = 31 * result + anchors.hashCode()
        result = 31 * result + orientation.hashCode()
        return result
    }

    override fun InspectorInfo.inspectableProperties() {
        debugInspectorInfo {
            properties["state"] = state
            properties["anchors"] = anchors
            properties["orientation"] = orientation
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private class DraggableAnchorsNode<T>(
    var state: AnchoredDraggableState<T>,
    var anchors: (size: IntSize, constraints: Constraints) -> Pair<DraggableAnchors<T>, T>,
    var orientation: Orientation
) : Modifier.Node(), LayoutModifierNode {
    private var didLookahead: Boolean = false

    override fun onDetach() {
        didLookahead = false
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        if (!isLookingAhead || !didLookahead) {
            val size = IntSize(placeable.width, placeable.height)
            anchors(size, constraints).run { state.updateAnchors(first, second) }
        }
        didLookahead = isLookingAhead || didLookahead
        return layout(placeable.width, placeable.height) {
            val offset = if (isLookingAhead) {
                state.anchors.positionOf(state.targetValue)
            } else {
                state.requireOffset()
            }
            val xOffset = if (orientation == Orientation.Horizontal) offset else 0f
            val yOffset = if (orientation == Orientation.Vertical) offset else 0f
            placeable.place(xOffset.roundToInt(), yOffset.roundToInt())
        }
    }
}

@Composable
@Preview
fun PreviewDesignBottomSheetScaffold() {
    PeerTheme {
        val state = remember {
            mutableStateOf<DesignBottomSheetScaffoldState>(DesignBottomSheetScaffoldState.HIDE)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            DesignButton({
                state.value = DesignBottomSheetScaffoldState.EXPAND
            }) { Text("Expand") }
            DesignBottomSheetScaffold(
                state = state.value,
                onDismiss = { state.value = DesignBottomSheetScaffoldState.HIDE }
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(MaterialTheme.colorScheme.tertiaryContainer))
            }
        }
    }
}
