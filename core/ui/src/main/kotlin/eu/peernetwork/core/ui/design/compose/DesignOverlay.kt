package eu.peernetwork.core.ui.design.compose

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

interface DesignOverlayBuilder {
    fun overlay(zIndex: Float = 1f, content: @Composable () -> Unit)
}

interface DesignOverlayController {
    fun show(tag: String)

    fun isVisible(tag: String): Boolean

    fun dismiss(tag: String)
}

private interface DesignOverlayRegistry {
    fun get(tag: String): @Composable (() -> Unit)?

    fun attach(tag: String, zIndex: Float, visible: Boolean, content: @Composable () -> Unit)

    fun detach(tag: String)
}

@Composable
fun DesignOverlay(
    startDestination: String,
    state: State<Boolean>,
    modifier: Modifier = Modifier,
    behind: Boolean = false,
    propagateMinConstraints: Boolean = false,
    contentAlignment: Alignment = Alignment.BottomCenter,
    onDismissRequest: () -> Unit,
    content: @Composable (NavHostController) -> Unit,
) {
    val updatedContent by rememberUpdatedState(content)
    DesignDialog(
        tag = startDestination,
        state = state,
        behind = behind,
        onDismissRequest = onDismissRequest
    ) { controller, animation, cancelable ->
        Box(
            modifier = modifier,
            contentAlignment = contentAlignment,
            propagateMinConstraints = propagateMinConstraints
        ) {
            val visibility = remember { mutableStateOf(false) }
            val offset = with(LocalDensity.current) { 56.dp.toPx() }
            Box(modifier = Modifier.graphicsLayer {
                alpha = animation.value
                translationY = (1 - animation.value) * offset
            }.background(MaterialTheme.colorScheme.background)) { updatedContent(controller) }
            LaunchedEffect(state.value) {
                visibility.value = state.value
            }
        }
    }
}

@Deprecated("""
    switch to
    fun DesignOverlay(
        tag: String,
        state: State<Boolean>,
        modifier: Modifier = Modifier,
        behind: Boolean = false,
        propagateMinConstraints: Boolean = false,
        contentAlignment: Alignment = Alignment.BottomCenter,
        onDismissRequest: () -> Unit,
        content: @Composable (NavHostController) -> Unit,
    ) 
""")
@Composable
fun DesignOverlay(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val routes = remember { linkedSetOf<String?>(null).toMutableStateList() }
    val registry = remember { mutableStateMapOf<String, Pair<Float, @Composable () -> Unit>>() }
    val currentController = remember(routes) {
        object : DesignOverlayController {
            override fun show(tag: String) {
                if (!routes.contains(tag)) {
                    routes.add(tag)
                }
            }

            override fun isVisible(tag: String): Boolean {
                return routes.contains(tag)
            }

            override fun dismiss(tag: String) {
                routes.remove(tag)
            }
        }
    }
    val updatedContent by rememberUpdatedState(content)
    val overlayRegistry = remember {
        object : DesignOverlayRegistry {
            override fun attach(
                tag: String,
                zIndex: Float,
                visible: Boolean,
                content: @Composable (() -> Unit)
            ) {
                registry[tag] = zIndex to content
                if (visible) {
                    routes.add(tag)
                }
            }

            override fun get(tag: String): @Composable (() -> Unit)? {
                return registry[tag]?.second
            }

            override fun detach(tag: String) {
                routes.remove(tag)
                registry.remove(tag)
            }
        }
    }
    CompositionLocalProvider(
        LocalDesignOverlayRegistry provides overlayRegistry,
        LocalDesignOverlayController provides currentController,
    ) {
        Box(modifier) {
            updatedContent()
            routes.filterNotNull().forEach { tag ->
                key(tag) {
                    Box(Modifier.zIndex(registry[tag]?.first ?: registry.size.toFloat())) {
                        registry[tag]?.second?.invoke()
                    }
                }
            }
        }
    }
}

@Composable
fun DesignOverlayHost(
    tag: String,
    visible: MutableState<Boolean>,
    handleBackPress: Boolean = true,
    onDismiss: () -> Unit = {},
    onAnimationComplete: (Boolean) -> Unit = {},
    durationMillis: Int = DefaultDurationMillis,
    builder: DesignOverlayBuilder.(State<Boolean>) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val controller = rememberDesignOverlayController()
    val isVisible = remember { mutableStateOf(visible.value) }
    val overlayRegistry = LocalDesignOverlayRegistry.current
    val overlayBuilder = remember(tag) {
        object : DesignOverlayBuilder {
            override fun overlay(zIndex: Float, content: @Composable () -> Unit) {
                overlayRegistry.attach(tag, zIndex, visible.value, content)
            }
        }
    }
    val updatedBuilder by rememberUpdatedState(builder)
    val observer = remember {
        LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                updatedBuilder(overlayBuilder, isVisible)
            } else if (event == Lifecycle.Event.ON_STOP && !isVisible.value) {
                overlayRegistry.detach(tag)
            }
        }
    }
    val handleOnDismiss by rememberUpdatedState(onDismiss)
    val handleOnAnimationComplete by rememberUpdatedState(onAnimationComplete)
    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(observer)
    }
    DisposableEffect(Unit) {
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    LaunchedEffect(visible.value) {
        snapshotFlow { visible.value }
            .collectLatest { value ->
                if (value) {
                    controller.show(tag)
                    delay(50)
                    isVisible.value = true
                    delay(durationMillis.toLong())
                    handleOnAnimationComplete(isVisible.value)
                } else {
                    isVisible.value = false
                    handleOnDismiss()
                    delay(durationMillis.toLong())
                    controller.dismiss(tag)
                    handleOnAnimationComplete(isVisible.value)
                }
            }
    }
    BackHandler(enabled = visible.value && handleBackPress) { visible.value = false }
}

@Composable
fun DesignOverlayPage(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignTitleBar {
        Box(modifier = modifier) {
            updatedContent()
        }
    }
}

@Composable
fun DesignOverlayBackground(
    state: State<Boolean>,
    modifier: Modifier = Modifier,
    durationMillis: Int = DefaultDurationMillis,
    delayMillis: Int = 0,
    easing: Easing = FastOutSlowInEasing
) {
    val alpha by animateFloatAsState(
        targetValue = if (state.value) 1f else 0f,
        animationSpec = tween(
            delayMillis = delayMillis,
            durationMillis = durationMillis,
            easing = easing
        ),
        label = "overlayAlphaAnimation"
    )
    Box(modifier = Modifier.graphicsLayer { this.alpha = alpha }) {
        Box(modifier = modifier.graphicsLayer {
            scaleX = if (state.value) {
                1f
            } else {
                0f
            }
            scaleY = scaleX
        }.pointerInput(Unit) {})
    }
}

val LocalDesignOverlayController = staticCompositionLocalOf<DesignOverlayController> {
    error("No DesignOverlayController provided")
}

private val LocalDesignOverlayRegistry = staticCompositionLocalOf<DesignOverlayRegistry> {
    error("No DesignOverlayRegistry provided")
}

@Composable
fun rememberDesignOverlayController(): DesignOverlayController {
    return LocalDesignOverlayController.current
}

@Composable
@Preview
fun PreviewDesignOverlay() {
    PeerTheme {
        DesignOverlay(modifier = Modifier.fillMaxSize()) {
            val tag = "peer"
            val state = remember { mutableStateOf(false) }
            val controller = rememberDesignOverlayController()
            Column {
                Button(onClick = {
                    if (controller.isVisible(tag)) {
                        state.value = false
                        controller.dismiss(tag)
                    } else {
                        state.value = true
                        controller.show(tag)
                    }
                }) { Text("toggle overlay") }
                DesignOverlayBackground(
                    state = state,
                    modifier = Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                )
                DesignOverlayHost(tag, remember { mutableStateOf(false) }) {
                    overlay {
                        Text("Hello, world!",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxSize()
                                .padding(24.dp)
                                .padding(top = 150.dp)
                        )
                    }
                }
            }
        }
    }
}
