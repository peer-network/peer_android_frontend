package eu.peernetwork.media.ui.attachment

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun AttachmentOption(
    onSquareClick: () -> Unit,
    onPortraitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AttachmentOption(
                painterResource(R.drawable.ic_square),
                stringResource(eu.peernetwork.media.ui.R.string.square),
                stringResource(eu.peernetwork.media.ui.R.string.square_label),
                6.dp,
                onSquareClick
            )
            AttachmentOption(
                painterResource(R.drawable.ic_vertical),
                stringResource(eu.peernetwork.media.ui.R.string.portrait),
                stringResource(eu.peernetwork.media.ui.R.string.portrait_label),
                3.dp,
                onPortraitClick
            )
        }
    }
}

@Composable
fun AttachmentOption(
    painter: Painter,
    text: String,
    label: String,
    space: Dp,
    onClick: () -> Unit = {}
) {
    val textStyle = MaterialTheme.typography.bodyMedium
        .copy(color = MaterialTheme.colorScheme.tertiary)
    val labelStyle = MaterialTheme.typography.labelMedium
        .copy(color = MaterialTheme.colorScheme.tertiary)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(8.dp)
            .clickable(
                enabled = true,
                role = Role.Button,
                onClick = onClick
            )
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.width(space))
        Text(text, style = textStyle)
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, style = labelStyle)
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewAttachmentOption() {
    PeerTheme {
        AttachmentOption({}, {})
    }
}
