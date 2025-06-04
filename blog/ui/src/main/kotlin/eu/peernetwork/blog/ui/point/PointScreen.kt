package eu.peernetwork.blog.ui.point

import androidx.compose.animation.AnimatedVisibility // for fade in/out
import androidx.compose.animation.fadeIn // added for fade in
import androidx.compose.animation.fadeOut // added for fade out
import androidx.compose.animation.core.tween // for custom duration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.model.UiPoint
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignOption
import eu.peernetwork.core.ui.extension.builder
import kotlinx.coroutines.delay // used for tooltip delay

@Composable
fun PointScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Point.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = PointViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val points = remember { mutableStateOf<List<UiPoint>?>(null) }


    if (points.value == null) {
        Box {}
    } else {
        PointScreen(points.value!!)
    }

    LaunchedEffect(state) {
        when (state) {
            is PointViewModel.State.Initialize -> viewModel.getPoints()
            is PointViewModel.State.Loading -> points.value = null
            is PointViewModel.State.Success -> {
                points.value = (state as PointViewModel.State.Success).points
            }
            is PointViewModel.State.Error -> {}
        }
    }
}

@Composable
fun PointScreen(points: List<UiPoint> = listOf()) {
    var selectedPoint by remember { mutableStateOf<UiPoint?>(null) }

    // State to control tooltip visibility
    var tooltipVisible by remember { mutableStateOf(false) }

    // Auto-show tooltip then fade out
    LaunchedEffect(selectedPoint) {
        if (selectedPoint != null) {
            tooltipVisible = true
            delay(1500) // show for 1.5 seconds
            tooltipVisible = false
        }
    }

    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            points.forEach { point ->
                PointModel.MAP[point.name]?.let { model ->
                    DesignOption(
                        text = point.available.toString(),
                        painter = painterResource(id = model.icon),
                        contentDescription = stringResource(model.label),
                        modifier = Modifier.padding(end = 1.dp),
                        onClick = {
                            selectedPoint = if (selectedPoint == point) null else point
                        }
                    )
                }
            }
        }

        // Tooltip with fadeIn + fadeOut animation
        selectedPoint?.let { point ->
            val model = PointModel.MAP[point.name]
            if (model != null) {
                val tooltipText = "You have ${point.available} Free ${stringResource(model.label)}"
                Popup(    //Popup
                    alignment = Alignment.TopStart,
                    offset = IntOffset(x = 0, y = 100),
                    properties = PopupProperties(focusable = false)
                ) {
                    AnimatedVisibility(
                        visible = tooltipVisible,
                        enter = fadeIn(animationSpec = tween(durationMillis = 300)), // fade in
                        exit = fadeOut(animationSpec = tween(durationMillis = 300))  // fade out
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shadowElevation = 4.dp,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = tooltipText,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
