package eu.peernetwork.media.ui.attachment

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.ui.R

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun AttachmentPlaceholder(
    modifier: Modifier = Modifier,
    onAttach: () -> Unit
) {
    val color = MaterialTheme.colorScheme.tertiaryContainer
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, end = 12.dp, top = 24.dp, bottom = 20.dp)
                .drawBehind {
                    val stroke = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 24f), 0f)
                    )
                    drawRoundRect(
                        color = color,
                        size = size,
                        style = stroke,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx())
                    )
                }.clip(RoundedCornerShape(24.dp))
                .clickable(role = Role.Button, onClick = onAttach)
        ) {
            Text(
                stringResource(R.string.media_label),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        IconButton(
            onClick = onAttach,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                painter = painterResource(eu.peernetwork.core.ui.R.drawable.ic_plus),
                contentDescription = stringResource(R.string.media_label),
                modifier = Modifier.padding(18.dp)
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewAttachmentPlaceholder() {
    PeerTheme {
        AttachmentPlaceholder(Modifier.aspectRatio(1f)) {}
    }
}
