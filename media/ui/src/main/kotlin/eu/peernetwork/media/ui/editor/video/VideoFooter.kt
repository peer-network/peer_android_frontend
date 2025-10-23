package eu.peernetwork.media.ui.editor.video

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.material.DesignOutlinedButton
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.ui.R
import eu.peernetwork.media.ui.compose.VideoRange
import eu.peernetwork.media.ui.extension.offset
import java.util.Locale
import kotlin.math.floor

@Composable
fun ColumnScope.VideoFooter(
    start: MutableState<Long>,
    stop: MutableState<Long>,
    duration: Long,
    frameSize: Int,
    minFrameSize: Int,
    timestamp: Long,
    scrollState: LazyListState,
    onDiscard: () -> Unit,
    onProceed: () -> Unit,
    thumbnail: @Composable (Long) -> Unit,
) {
    Spacer(modifier = Modifier.height(32.dp))
    VideoRange(
        start = start,
        stop = stop,
        duration = duration,
        state = scrollState,
        frameSize = frameSize,
        minFrameSize = minFrameSize,
        content = thumbnail
    )
    VideoFooter(start, stop, timestamp, modifier = Modifier
        .padding(top = 16.dp)
        .padding(horizontal = 24.dp)
        .align(Alignment.End), onDiscard, onProceed)
}

@Composable
private fun VideoFooter(
    start: MutableState<Long>,
    stop: MutableState<Long>,
    timestamp: Long,
    modifier: Modifier = Modifier,
    onDiscard: () -> Unit = {},
    onProceed: () -> Unit,
) {
    val startTime = remember { derivedStateOf { timestamp.offset(start.value).toSecondsString() } }
    val stopTime = remember { derivedStateOf { timestamp.offset(stop.value).toSecondsString() } }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DesignOutlinedButton(
            onClick = onProceed,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(28)
                ),
            enabled = true,
            isLoading = false,
            shape = RoundedCornerShape(28),
            textStyle = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.surfaceDim,
            ),
            minHeight = 36.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp)
        ) { Text(stringResource(R.string.continue_label)) }
        Spacer(modifier = Modifier.width(12.dp))
        DesignOutlinedButton(
            onClick = onDiscard,
            enabled = true,
            isLoading = false,
            shape = RoundedCornerShape(28),
            textStyle = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.surfaceDim,
            ),
            minHeight = 36.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp)
        ) { Text(stringResource(R.string.discard_label)) }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            startTime.value,
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.tertiary
            )
        )
        Box(modifier = Modifier.padding(8.dp)
            .size(4.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.tertiary.copy(alpha = .5f)))
        Text(
            stopTime.value,
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.tertiary
            )
        )
    }
}

fun Long.toSecondsString(locale: Locale = Locale.US): String {
    val totalSeconds = this / 1000.0
    val hours = floor(totalSeconds / 3600).toInt()
    val minutes = floor((totalSeconds % 3600) / 60).toInt()
    val seconds = totalSeconds % 60
    return when {
        hours > 0 -> String.format(locale, "%d:%02d:%05.2f", hours, minutes, seconds)
        minutes > 0 -> String.format(locale, "%d:%05.2f", minutes, seconds)
        else -> String.format(locale, "%.2fs", seconds)
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewVideoFooter() {
    PeerTheme {
        val start = remember { mutableLongStateOf(0L) }
        val stop = remember { mutableLongStateOf(10L) }
        Column(modifier = Modifier.fillMaxWidth()) {
            VideoFooter(start, stop, 0L, modifier = Modifier
                .padding(vertical = 12.dp)
                .padding(horizontal = 24.dp)
                .align(Alignment.End)) {}
        }
    }
}
