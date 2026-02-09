package eu.peernetwork.ads.ui.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.ads.ui.model.UiStatus
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.ads.ui.R

@Composable
fun AnalyticsLabel(
    color: Color,
    painter: Painter,
    label: String,
    contentDescription: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(vertical = 4.dp)
            .padding(horizontal = 8.dp)
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            text = contentDescription,
            color = color,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
                .padding(start = 16.dp)
        )
    }
}

@Composable
fun AnalyticsVisibilityLabel(status: UiStatus) {
    if (status == UiStatus.ILLEGAL) {
        Text(
            text = stringResource(R.string.illegal_description),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.clip(CircleShape)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(vertical = 4.dp)
                .padding(horizontal = 8.dp)
        )
    } else {
        AnalyticsLabel(
            color = MaterialTheme.colorScheme.outline,
            painter = painterResource(R.drawable.ic_hidden),
            label = stringResource(R.string.hidden_label),
            contentDescription = stringResource(R.string.hidden_description)
        )
    }
}

@Preview
@Composable
fun PreviewAnalyticsLabel() {
    DesignTheme(isDarkMode = true) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AnalyticsVisibilityLabel(UiStatus.ILLEGAL)
            AnalyticsVisibilityLabel(UiStatus.VISIBLE)
        }
    }
}
