package eu.peernetwork.wallet.ui.reward

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiReward

@Composable
fun RewardPopup(
    state: MutableState<Boolean>,
    selected: MutableState<UiReward?>,
) {
    val context = LocalContext.current
    if (state.value) {
        Popup(
            alignment = Alignment.TopStart,
            offset = IntOffset(x = 0, y = 22.dp.value.let {
                it * context.resources.displayMetrics.density
            }.toInt()),
            onDismissRequest = { state.value = false }
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 4.dp,
                shape = RoundedCornerShape(6.dp)
            ) {
                Crossfade(selected.value) { target ->
                    target?.let {
                        RewardType.MAP[target.name]?.let { model ->
                            Text(
                                text = stringResource(R.string.reward_description, target.available, target.name),
                                color = MaterialTheme.colorScheme.outline,
                                fontWeight = FontWeight.Normal,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
