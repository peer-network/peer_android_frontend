package eu.peernetwork.media.ui.editor.video

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.media.ui.R
import eu.peernetwork.media.ui.compose.VideoRange

@Composable
fun ColumnScope.VideoFooter(
    start: MutableState<Long>,
    stop: MutableState<Long>,
    duration: Long,
    frameSize: Int,
    minFrameSize: Int,
    scrollState: LazyListState,
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
    DesignOutlinedButton(
        onClick = onProceed,
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp)
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(28)
            )
            .align(Alignment.End),
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
}
