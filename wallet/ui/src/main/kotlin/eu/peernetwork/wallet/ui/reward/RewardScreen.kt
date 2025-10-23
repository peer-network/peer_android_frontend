package eu.peernetwork.wallet.ui.reward

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignLabeledIcon
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.wallet.ui.model.UiReward

@Composable
fun RewardScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Reward.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = RewardViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val points = remember { derivedStateOf { (state as? RewardViewModel.State.Success?)?.rewards } }
    Crossfade(targetState = points.value) { target ->
        if (target != null) {
            RewardScreen(target)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getRewards()
    }
}

@Composable
fun RewardScreen(points: List<UiReward> = listOf()) {
    var showPopup = remember { mutableStateOf(false) }
    var selectedPoint = remember { mutableStateOf<UiReward?>(null) }
    Column {
        Row (
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            points.forEach { point ->
                RewardType.MAP[point.name]?.let { model ->
                    val enabled = remember { derivedStateOf {
                        showPopup.value && selectedPoint.value?.name == point.name
                    } }
                    Box {
                        DesignLabeledIcon(
                            text = point.available.toString(),
                            painter = painterResource(id = model.icon),
                            contentDescription = stringResource(model.label),
                            onClick = {
                                showPopup.value = true
                                selectedPoint.value = point
                            },
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = .8f),
                            modifier = Modifier.graphicsLayer {
                                alpha = if (enabled.value) {
                                    1f
                                } else {
                                    .0f
                                }
                            }
                        )
                        DesignLabeledIcon(
                            text = point.available.toString(),
                            painter = painterResource(id = model.icon),
                            contentDescription = stringResource(model.label),
                            onClick = {
                                showPopup.value = true
                                selectedPoint.value = point
                            },
                            modifier = Modifier.graphicsLayer {
                                alpha = if (enabled.value) {
                                    0f
                                } else {
                                    1f
                                }
                            }
                        )
                    }
                }
            }
        }
    }
    RewardPopup(showPopup, selectedPoint)
}
