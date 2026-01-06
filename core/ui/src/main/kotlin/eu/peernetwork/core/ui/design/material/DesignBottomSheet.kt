package eu.peernetwork.core.ui.design.material

import android.annotation.SuppressLint
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class DesignBottomSheetState { EXPAND, COLLAPSE, HIDE }

@Composable
fun DesignBottomSheet(
    state: State<Boolean>,
    dim: Boolean = true,
    canDismiss: () -> Boolean = { true },
    color: Color = MaterialTheme.colorScheme.surfaceDim,
    peekHeight: Dp = 400.dp,
    orientation: Orientation = Orientation.Vertical,
    confirmValueChange: (DesignBottomSheetState) -> Boolean = { true },
    onStateChanged: (DesignBottomSheetState) -> Unit = {},
    onDismiss: () -> Unit = {},
    snapAnimationSpec: AnimationSpec<Float> = tween(
        durationMillis = 250,
        easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)
    ),
    footer: @Composable () -> Unit = {},
    overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable (State<Size>) -> Unit
) {
    val visible = remember { mutableStateOf(false) }
    val updatedContent by rememberUpdatedState(content)
    val handleOnDismiss by rememberUpdatedState(onDismiss)
    val dialogState = remember { mutableStateOf(DesignBottomSheetState.HIDE) }
    DesignDialog(
        state = visible,
        dim = dim,
        onDismiss = {
            visible.value = false
            handleOnDismiss()
        },
        canDismiss = canDismiss,
        onBackPressed = { dialogState.value = DesignBottomSheetState.HIDE }
    ) { controller, anim, cancelable ->
        val isDismissed = remember(anim.value, visible.value) {
            derivedStateOf { !visible.value && anim.value == 0f }
        }
        DesignBottomSheet(
            state = dialogState.value,
            color = color,
            peekHeight = peekHeight,
            orientation = orientation,
            confirmValueChange = confirmValueChange,
            onStateChanged = onStateChanged,
            onDismiss = {
                visible.value = false
                dialogState.value = DesignBottomSheetState.HIDE
                handleOnDismiss()
            },
            snapAnimationSpec = snapAnimationSpec,
            footer = footer,
            overlay = overlay
        ) { updatedContent(it) }
        LaunchedEffect(isDismissed.value) {
            if (isDismissed.value) {
                handleOnDismiss()
            }
        }
        LaunchedEffect(state.value) {
            if (state.value) {
                dialogState.value = DesignBottomSheetState.COLLAPSE
            }
        }
    }
    LaunchedEffect(state.value) {
        if (state.value) {
            visible.value = true
        } else {
            dialogState.value = DesignBottomSheetState.HIDE
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
fun DesignBottomSheet(
    state: DesignBottomSheetState,
    color: Color = MaterialTheme.colorScheme.tertiaryContainer,
    shape: Shape = ShapeDefaults.ExtraLarge.copy(
        bottomStart = CornerSize(0.0.dp),
        bottomEnd = CornerSize(0.0.dp)
    ),
    orientation: Orientation = Orientation.Vertical,
    confirmValueChange: (DesignBottomSheetState) -> Boolean = { true },
    onStateChanged: (DesignBottomSheetState) -> Unit = {},
    onDismiss: () -> Unit = {},
    peekHeight: Dp,
    snapAnimationSpec: AnimationSpec<Float> = tween(
        durationMillis = 350,
        easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)
    ),
    footer: @Composable () -> Unit = {},
    overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable (State<Size>) -> Unit
) {
    val density = LocalDensity.current
    val updatedFooter by rememberUpdatedState(footer)
    val updatedOverlay by rememberUpdatedState(overlay)
    val updatedContent by rememberUpdatedState(content)
    val handleStateChanged by rememberUpdatedState(onStateChanged)
    val handleDismiss by rememberUpdatedState(onDismiss)
    val settledState = remember { mutableStateOf(state) }
    val peekHeightPx = remember { with(density) { peekHeight.toPx() } }
    val positionalThreshold = with(density) { 56.dp.toPx() }
    val draggableState = remember {
        AnchoredDraggableState(
            initialValue = state,
            positionalThreshold = { positionalThreshold },
            velocityThreshold = { with(density) { 125.dp.toPx() } },
            snapAnimationSpec = snapAnimationSpec,
            decayAnimationSpec = exponentialDecay(),
            confirmValueChange = confirmValueChange,
        )
    }
    val scope = rememberCoroutineScope()
    val nestedScroll = remember(draggableState) {
        bottomSheetNestedScrollConnection(draggableState, orientation) {
            scope.launch { draggableState.settle(it) }
        }
    }
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val height = with(density) { maxHeight.toPx() }
        val footerHeight = remember { mutableStateOf(Size.Zero) }
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.nestedScroll(nestedScroll)
                .draggableAnchors(draggableState, orientation) { sheetSize, constraints ->
                    val layoutHeight = constraints.maxHeight.toFloat()
                    DraggableAnchors {
                        DesignBottomSheetState.EXPAND at 0f
                        DesignBottomSheetState.COLLAPSE at layoutHeight - peekHeightPx
                        DesignBottomSheetState.HIDE at layoutHeight
                    } to state
                }.anchoredDraggable(
                    state = draggableState,
                    orientation = orientation,
                )
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(shape)
                .background(color)
        ) { updatedContent(footerHeight) }
        Box(modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
            .graphicsLayer {
                val position = (height - size.height)
                val delta = (draggableState.offset - (peekHeightPx + (positionalThreshold * .9f)))
                footerHeight.value = size
                translationY = (position + delta).coerceIn(position, height)
            }) { updatedFooter() }
        Box(
            modifier = Modifier.heightIn(min = peekHeight)
                .graphicsLayer {
                    val position = (height - size.height)
                    translationY = (position + draggableState.offset).coerceIn(position, height)
                }.clip(shape)
        ) { updatedOverlay() }
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
    DisposableEffect(draggableState.isAnimationRunning) {
        onDispose {
            if (!draggableState.isAnimationRunning &&
                draggableState.currentValue == DesignBottomSheetState.HIDE) {
                handleDismiss()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
internal fun bottomSheetNestedScrollConnection(
    state: AnchoredDraggableState<DesignBottomSheetState>,
    orientation: Orientation,
    onFling: (velocity: Float) -> Unit
) : NestedScrollConnection = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.toFloat()
        return if (delta < 0 && source == NestedScrollSource.UserInput) {
            state.dispatchRawDelta(delta).toOffset()
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        return if (source == NestedScrollSource.UserInput) {
            state.dispatchRawDelta(available.toFloat()).toOffset()
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val toFling = available.toFloat()
        val currentOffset = state.requireOffset()
        val minAnchor = state.anchors.minAnchor()
        return if (toFling < 0 && currentOffset > minAnchor) {
            onFling(toFling)
            available
        } else {
            Velocity.Zero
        }
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        onFling(available.toFloat())
        return available
    }

    private fun Float.toOffset(): Offset =
        Offset(
            x = if (orientation == Orientation.Horizontal) this else 0f,
            y = if (orientation == Orientation.Vertical) this else 0f
        )

    @JvmName("velocityToFloat")
    private fun Velocity.toFloat() = if (orientation == Orientation.Horizontal) x else y

    @JvmName("offsetToFloat")
    private fun Offset.toFloat(): Float = if (orientation == Orientation.Horizontal) x else y
}

@OptIn(ExperimentalFoundationApi::class)
internal fun <T> Modifier.draggableAnchors(
    state: AnchoredDraggableState<T>,
    orientation: Orientation,
    anchors: (size: IntSize, constraints: Constraints) -> Pair<DraggableAnchors<T>, T>,
) = this then BottomSheetDraggableAnchorsElement(state, anchors, orientation)

@OptIn(ExperimentalFoundationApi::class)
internal class BottomSheetDraggableAnchorsElement<T>(
    private val state: AnchoredDraggableState<T>,
    private val anchors: (size: IntSize, constraints: Constraints) -> Pair<DraggableAnchors<T>, T>,
    private val orientation: Orientation
) : ModifierNodeElement<BottomSheetDraggableAnchorsNode<T>>() {

    override fun create() = BottomSheetDraggableAnchorsNode(state, anchors, orientation)

    override fun update(node: BottomSheetDraggableAnchorsNode<T>) {
        node.state = state
        node.anchors = anchors
        node.orientation = orientation
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is BottomSheetDraggableAnchorsElement<*>) return false

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
internal class BottomSheetDraggableAnchorsNode<T>(
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
@OptIn(ExperimentalMaterial3Api::class)
fun PreviewDesignBottomSheet() {
    PeerTheme {
        val state = remember {
            mutableStateOf(DesignBottomSheetState.HIDE)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            DesignBottomSheet(
                state = state.value,
                peekHeight = 400.dp,
                onDismiss = { state.value = DesignBottomSheetState.HIDE },
                footer = {
                    Box(modifier = Modifier.fillMaxWidth()
                        .height(150.dp)
                        .background(MaterialTheme.colorScheme.secondary))
                }
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.tertiaryContainer))
            }
            DesignButton({
                state.value = DesignBottomSheetState.COLLAPSE
            }) { Text("Expand") }
        }
    }
}
