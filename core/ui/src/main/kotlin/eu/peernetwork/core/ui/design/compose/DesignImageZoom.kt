package eu.peernetwork.core.ui.design.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch

@Composable
fun DesignImageZoom(
    imageUrl: MutableState<String?>,
    onDismiss: () -> Unit = {}
) {
    val key = remember { System.currentTimeMillis().toString() }
    val visible = remember(imageUrl.value) { mutableStateOf(imageUrl.value != null) }
    DesignDialog(
        key,
        visible,
        onDismiss = onDismiss,
        onAnimationComplete = {
            if (!it) {
                imageUrl.value = null
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl.value)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(200.dp)
            )
            val offsetX = remember { mutableFloatStateOf(0f) }
            val offsetY = remember { mutableFloatStateOf(0f) }
            val scale = remember { Animatable(1f) }
            val scope = rememberCoroutineScope()
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUrl.value,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .offset {
                            if (scale.value == 1f) {
                                IntOffset(offsetX.floatValue.toInt(), offsetY.floatValue.toInt())
                            } else {
                                IntOffset(0, 0)
                            }
                        }
                        .graphicsLayer {
                            scaleX = scale.value
                            scaleY = scale.value
                            shadowElevation = 19.dp.toPx()
                            shape = CircleShape
                            clip = true
                        }
                        .pointerInput(Unit) {
                            forEachGesture {
                                awaitPointerEventScope {
                                    var zooming = false
                                    do {
                                        val event = awaitPointerEvent()
                                        val zoomChange = event.calculateZoom()
                                        val pan = event.calculatePan()

                                        if (zoomChange != 1f) zooming = true

                                        val newScale = (scale.value * zoomChange).coerceIn(1f, 2f)
                                        scope.launch {
                                            scale.snapTo(newScale)
                                        }
                                        if (scale.value == 1f) {
                                            offsetX.floatValue += pan.x * 0.6f
                                            offsetY.floatValue += pan.y * 0.6f
                                        }
                                    } while (event.changes.any { it.pressed })
                                    if (zooming) {
                                        scope.launch {
                                            scale.animateTo(
                                                1f,
                                                animationSpec = tween(durationMillis = 350)
                                            )
                                        }
                                        scope.launch {
                                            animate(
                                                initialValue = offsetX.floatValue,
                                                targetValue = 0f,
                                                animationSpec = tween(350)
                                            ) { value, _ -> offsetX.floatValue = value }
                                        }
                                        scope.launch {
                                            animate(
                                                initialValue = offsetY.floatValue,
                                                targetValue = 0f,
                                                animationSpec = tween(350)
                                            ) { value, _ -> offsetY.floatValue = value }
                                        }
                                    }
                                }
                            }
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = {
                                    scope.launch {
                                        animate(
                                            initialValue = offsetX.floatValue,
                                            targetValue = 0f,
                                            animationSpec = tween(300)
                                        ) { value, _ -> offsetX.floatValue = value }
                                    }
                                    scope.launch {
                                        animate(
                                            initialValue = offsetY.floatValue,
                                            targetValue = 0f,
                                            animationSpec = tween(300)
                                        ) { value, _ -> offsetY.floatValue = value }
                                    }
                                }
                            ) { change, dragAmount ->
                                change.consume()
                                if (scale.value == 1f) {
                                    offsetX.floatValue += dragAmount.x * 0.6f
                                    offsetY.floatValue += dragAmount.y * 0.6f
                                }
                            }
                        }.size(240.dp)
                )
            }
        }
    }
}
