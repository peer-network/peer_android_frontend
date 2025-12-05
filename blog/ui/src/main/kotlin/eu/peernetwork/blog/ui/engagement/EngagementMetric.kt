package eu.peernetwork.blog.ui.engagement

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import eu.peernetwork.blog.ui.R
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppDarkRed

@Composable
fun EngagementMetric(
    text: String,
    painter: Painter,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    checkedPainter: Painter = painter,
    color: Color = MaterialTheme.colorScheme.onBackground,
    tint: Color = color,
    checkedTint: Color = PeerAppDarkRed,
    contentDescription: String? = null,
    size: Dp = 20.dp,
    style: TextStyle = MaterialTheme.typography.labelLarge,
    orientation: Orientation = Orientation.Horizontal,
    onClick: () -> Unit
) {
    if (orientation == Orientation.Vertical) {
        EngagementMetric(
            text = text,
            painter = painter,
            modifier = modifier,
            color = color,
            checked = checked,
            checkedPainter = checkedPainter,
            tint = tint,
            checkedTint = checkedTint,
            contentDescription = contentDescription,
            size = size,
            style = style,
            horizontalAlignment = Alignment.CenterHorizontally,
            onClick = onClick
        )
    } else {
        EngagementMetric(
            text = text,
            painter = painter,
            modifier = modifier,
            color = color,
            checked = checked,
            checkedPainter = checkedPainter,
            tint = tint,
            checkedTint = checkedTint,
            contentDescription = contentDescription,
            size = size,
            style = style,
            verticalAlignment = Alignment.CenterVertically,
            onClick = onClick
        )
    }
}

@Composable
fun EngagementMetric(
    text: String,
    painter: Painter,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    checkedPainter: Painter = painter,
    color: Color = MaterialTheme.colorScheme.onBackground,
    tint: Color = color,
    checkedTint: Color = PeerAppDarkRed,
    contentDescription: String? = null,
    size: Dp = 20.dp,
    style: TextStyle = MaterialTheme.typography.labelLarge,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable(
            onClick = onClick
        ),
        horizontalAlignment = horizontalAlignment
    ) {
        Icon(
            painter = if (checked) {
                checkedPainter
            } else {
                painter
            },
            contentDescription = contentDescription,
            tint = if (checked) {
                checkedTint
            } else {
                tint
            },
            modifier = Modifier.size(size)
        )
        Text(
            text = text,
            style = style,
            color = color
        )
    }
}

@Composable
fun EngagementMetric(
    text: String,
    painter: Painter,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    checkedPainter: Painter = painter,
    color: Color = MaterialTheme.colorScheme.onBackground,
    tint: Color = color,
    checkedTint: Color = PeerAppDarkRed,
    contentDescription: String? = null,
    size: Dp = 20.dp,
    style: TextStyle = MaterialTheme.typography.labelLarge,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(3.dp),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.clickable(
            onClick = onClick
        ),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        Icon(
            painter = if (checked) {
                checkedPainter
            } else {
                painter
            },
            contentDescription = contentDescription,
            tint = if (checked) {
                checkedTint
            } else {
                tint
            },
            modifier = Modifier.size(size)
        )
        Text(
            text = text,
            style = style,
            color = color
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun PreviewEngagementMetric() {
    DesignTheme(isDarkMode = false) {
        Column(modifier = Modifier.padding(16.dp)) {
            EngagementMetric(
                text = "5k",
                checked = true,
                painter = painterResource(R.drawable.ic_love_outline),
                checkedPainter = painterResource(R.drawable.ic_love),
                orientation = Orientation.Vertical,
                modifier = Modifier.align(Alignment.End)
            ) {}
            EngagementMetric(
                text = "5k",
                painter = painterResource(R.drawable.ic_love_outline),
                orientation = Orientation.Horizontal
            ) {}
        }
    }
}
