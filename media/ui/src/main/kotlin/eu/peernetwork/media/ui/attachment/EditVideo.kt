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
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.ui.R

@Composable
fun EditVideo(
    onEditVideoClick: () -> Unit,
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
            EditVideo(
                painterResource(R.drawable.cut),
                stringResource(R.string.trim_label),
                6.dp,
                onEditVideoClick
            )
        }
    }
}

@Composable
fun EditVideo(
    painter: Painter,
    text: String,
    space: Dp,
    onClick: () -> Unit = {}
) {
    val textStyle = MaterialTheme.typography.bodyMedium
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
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewEditVideo() {
    PeerTheme {
        EditVideo({})
    }
}