package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppRed

@Composable
fun PostLabel(
    color: Color,
    painter: Painter,
    contentDescription: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(vertical = 2.dp)
            .padding(horizontal = 6.dp)
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = color,
            modifier = Modifier.size(10.dp)
        )
        Text(
            text = contentDescription,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
fun PostReportLabel() {
    PostLabel(
        color = PeerAppRed,
        painter = painterResource(R.drawable.ic_flag),
        contentDescription = stringResource(R.string.reported_label)
    )
}

@Composable
fun PostVisibilityLabel() {
    PostLabel(
        color = MaterialTheme.colorScheme.outline,
        painter = painterResource(R.drawable.ic_hidden),
        contentDescription = stringResource(R.string.hidden_label)
    )
}

@Preview
@Composable
fun PreviewPostLabel() {
    DesignTheme(isDarkMode = true) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PostReportLabel()
            PostVisibilityLabel()
        }
    }
}
