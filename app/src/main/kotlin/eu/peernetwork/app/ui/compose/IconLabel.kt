package eu.peernetwork.app.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun IconLabel(
    painter: Painter,
    label: String,
    contentDescription: String? = label,
    modifier: Modifier = Modifier,
    size: Dp = 72.dp,
    space: Dp = 0.dp,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    color: Color = MaterialTheme.colorScheme.outline
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space)
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.size(size)
        )
        Text(
            text = label,
            style = style,
            color = color
        )
    }
}

@Composable
fun IconLabel(
    painter: Painter,
    label: String,
    contentDescription: String? = label,
    modifier: Modifier = Modifier,
    size: Dp = 72.dp,
    space: Dp = 0.dp,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    color: Color = MaterialTheme.colorScheme.outline,
    tint: Color = MaterialTheme.colorScheme.outline,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space)
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.size(size),
            tint = tint
        )
        Text(
            text = label,
            style = style,
            color = color
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun IconLabelPreview() {
    DesignTheme {
        IconLabel(
            painter = painterResource(id = R.drawable.bg_heart),
            label = "Engage",
            contentDescription = null,
            modifier = Modifier
        )
    }
}
