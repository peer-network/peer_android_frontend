package eu.peernetwork.blog.ui.point

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.model.UiPoint
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignLabeledIcon
import eu.peernetwork.core.ui.extension.builder

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
    val points = remember { mutableStateOf<List<UiPoint>?>(
        (state as? PointViewModel.State.Success?)?.points
    ) }
    Crossfade(targetState = points.value) {
        when (it) {
            null -> Box {}
            else -> PointScreen(it)
        }
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
    Column {
        Row (
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            points.forEach { point ->
                PointModel.MAP[point.name]?.let { model ->
                    DesignLabeledIcon(
                        text = point.available.toString(),
                        painter = painterResource(id = model.icon),
                        contentDescription = stringResource(model.label),
                        onClick = {  }
                    )
                }
            }
        }
    }
}
