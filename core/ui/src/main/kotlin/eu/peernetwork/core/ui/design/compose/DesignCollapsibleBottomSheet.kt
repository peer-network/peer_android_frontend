package eu.peernetwork.core.ui.design.compose

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.coroutines.launch

@Composable
fun DesignCollapsibleBottomSheet(
    state: State<Boolean>,
    behind: Boolean = true,
    peekHeight: Dp = 250.dp,
    color: Color = MaterialTheme.colorScheme.tertiaryContainer,
    orientation: Orientation = Orientation.Vertical,
    confirmValueChange: (DesignBottomSheetState) -> Boolean = { true },
    onStateChanged: (DesignBottomSheetState) -> Unit = {},
    onDismiss: () -> Unit = {},
    duration: Int = 350,
    snapAnimationSpec: AnimationSpec<Float> = tween<Float>(
        durationMillis = duration,
        easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)
    ),
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val handleOnDismiss by rememberUpdatedState(onDismiss)
    val dialogState = remember { mutableStateOf(DesignBottomSheetState.HIDE) }
    val visible = remember { mutableStateOf(false) }
    DesignDialog(
        state = visible,
        dim = behind,
        onDismiss = {
            visible.value = false
            handleOnDismiss()
        },
        duration = duration.toLong(),
        canDismiss = dialogState.value == DesignBottomSheetState.HIDE,
        onBackPressed = { dialogState.value = DesignBottomSheetState.HIDE }
    ) { controller, anim, cancelable ->
        val isDismissed = remember(anim.value, visible.value) {
            derivedStateOf { !visible.value && anim.value == 0f }
        }
        DesignCollapsibleBottomSheet(
            state = dialogState.value,
            peekHeight = peekHeight,
            color = color,
            orientation = orientation,
            confirmValueChange = confirmValueChange,
            onStateChanged = onStateChanged,
            snapAnimationSpec = snapAnimationSpec,
            onDismiss = {
                visible.value = false
                dialogState.value = DesignBottomSheetState.HIDE
                handleOnDismiss()
            },
        ) { updatedContent() }
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
fun DesignCollapsibleBottomSheet(
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
    snapAnimationSpec: AnimationSpec<Float> = tween<Float>(
        durationMillis = 350,
        easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)
    ),
    peekHeight: Dp,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val updatedContent by rememberUpdatedState(content)
    val handleStateChanged by rememberUpdatedState(onStateChanged)
    val handleDismiss by rememberUpdatedState(onDismiss)
    val settledState = remember { mutableStateOf(state) }
    val peekHeightPx = remember { with(density) { peekHeight.toPx() } }
    val draggableState = remember {
        AnchoredDraggableState(
            initialValue = state,
            positionalThreshold = { with(density) { 56.dp.toPx() } },
            velocityThreshold = { with(density) { 125.dp.toPx() } },
            snapAnimationSpec = snapAnimationSpec,
            decayAnimationSpec = exponentialDecay<Float>(),
            confirmValueChange = confirmValueChange,
        )
    }
    val scope = rememberCoroutineScope()
    val nestedScroll = remember(draggableState) {
        bottomSheetNestedScrollConnection(draggableState, orientation) {
            scope.launch { draggableState.settle(it) }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
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
    DisposableEffect(draggableState.isAnimationRunning) {
        onDispose {
            if (!draggableState.isAnimationRunning &&
                draggableState.currentValue == DesignBottomSheetState.HIDE) {
                handleDismiss()
            }
        }
    }
}

@Composable
@Preview
fun PreviewDesignCollapsibleBottomSheet() {
    PeerTheme {
        val state = remember {
            mutableStateOf<DesignBottomSheetState>(DesignBottomSheetState.HIDE)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            DesignButton({
                state.value = DesignBottomSheetState.COLLAPSE
            }) { Text("Expand") }
            DesignCollapsibleBottomSheet(
                state = state.value,
                peekHeight = 250.dp,
                onDismiss = { state.value = DesignBottomSheetState.HIDE },
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.tertiaryContainer))
            }
        }
    }
}
