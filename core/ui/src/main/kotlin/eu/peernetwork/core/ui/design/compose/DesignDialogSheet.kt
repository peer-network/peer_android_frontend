package eu.peernetwork.core.ui.design.compose

import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun  DesignDialogSheet(
    tag: String,
    visible: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    durationMillis: Int = DefaultDurationMillis,
    contentAlignment: Alignment = Alignment.TopStart,
    onDismiss: () -> Unit = {},
    onAnimationComplete: (Boolean) -> Unit = {},
    background: (@Composable () -> Unit)? = null,
    builder: @Composable (State<Boolean>) -> Unit,
) {
    DesignDialog(
        tag = tag,
        modifier = modifier,
        visible = visible,
        onDismiss = onDismiss,
        onAnimationComplete = onAnimationComplete,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = durationMillis)
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = durationMillis)
        ) + fadeOut(),
        durationMillis = durationMillis,
        contentAlignment =  contentAlignment,
        background = background,
        builder = builder
    )
}

enum class DesignDialogSheetState { EXPAND, COLLAPSE, HIDE }

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun DesignDialogSheet(
    state: DesignDialogSheetState,
    orientation: Orientation = Orientation.Vertical,
    confirmValueChange: (DesignDialogSheetState) -> Boolean = { true },
    onStateChanged: (DesignDialogSheetState) -> Unit = {},
    onDismiss: (DesignDialogSheetState) -> Unit = {},
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
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .draggableAnchors(draggableState, orientation) { sheetSize, constraints ->
                val layoutHeight = constraints.maxHeight.toFloat()
                val sheetHeight = sheetSize.height.toFloat()
                DraggableAnchors {
                    DesignDialogSheetState.EXPAND at 0f
                    DesignDialogSheetState.COLLAPSE at sheetHeight / 2
                    DesignDialogSheetState.HIDE at layoutHeight
                } to state
            }.anchoredDraggable(
                state = draggableState,
                orientation = orientation
            )
            .fillMaxWidth()
            .wrapContentHeight()
    ) { updatedContent() }
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
            if (draggableState.currentValue == DesignDialogSheetState.HIDE) {
                handleDismiss(draggableState.currentValue)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun <T> Modifier.draggableAnchors(
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
