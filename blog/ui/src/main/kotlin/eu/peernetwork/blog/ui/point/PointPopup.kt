package eu.peernetwork.blog.ui.point

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
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.model.UiPoint

@Composable
fun PointPopup(
    state: MutableState<Boolean>,
    selected: MutableState<UiPoint?>,
) {
    val context = LocalContext.current
    if (state.value) {
        Popup(
            alignment = Alignment.TopStart,
            offset = IntOffset(x = 0, y = 24.dp.value.let {
                it * context.resources.displayMetrics.density
            }.toInt()),
            onDismissRequest = { state.value = false }
        ) {
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shadowElevation = 4.dp,
                shape = RoundedCornerShape(6.dp)
            ) {
                Crossfade(selected.value) { target ->
                    target?.let {
                        PointModel.MAP[target.name]?.let { model ->
                            Text(
                                text = stringResource(R.string.point_description, target.available, target.name),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.Normal
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
