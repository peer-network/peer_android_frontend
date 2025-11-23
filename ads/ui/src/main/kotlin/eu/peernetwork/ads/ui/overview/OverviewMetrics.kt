package eu.peernetwork.ads.ui.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.ads.domain.model.Metrics
import eu.peernetwork.ads.ui.R
import eu.peernetwork.ads.ui.model.UiMetrics
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun OverviewMetrics(
    metrics: UiMetrics,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .then(modifier)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OverviewMetricsLabel(
            painter = painterResource(R.drawable.ic_likes),
            label = metrics.likes.toString(),
            modifier = Modifier.padding(vertical = 8.dp)
                .weight(1f)
        )
        OverviewDivider()
        OverviewMetricsLabel(
            painter = painterResource(R.drawable.ic_dislikes),
            label = metrics.dislikes.toString(),
            modifier = Modifier.padding(vertical = 8.dp)
                .weight(1f)
        )
        OverviewDivider()
        OverviewMetricsLabel(
            painter = painterResource(R.drawable.ic_comments),
            label = metrics.comments.toString(),
            modifier = Modifier.padding(vertical = 8.dp)
                .weight(1f)
        )
        OverviewDivider()
        OverviewMetricsLabel(
            painter = painterResource(R.drawable.ic_views),
            label = metrics.views.toString(),
            modifier = Modifier.padding(vertical = 8.dp)
                .weight(1f)
        )
        OverviewDivider()
        OverviewMetricsLabel(
            painter = painterResource(R.drawable.ic_reports),
            label = metrics.report.toString(),
            modifier = Modifier.padding(vertical = 8.dp)
                .weight(1f)
        )
    }
}

@Composable
private fun OverviewMetricsLabel(
    painter: Painter,
    label: String,
    contentDescription: String? = label,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    space: Dp = 8.dp,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.onBackground,
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
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun OverviewDivider() {
    Spacer(modifier = Modifier.width(1.dp)
        .height(48.dp)
        .background(MaterialTheme.colorScheme.surfaceContainerLow))
}

@Preview
@Composable
fun PreviewOverviewMetrics() {
    DesignTheme(isDarkMode = true) {
        val metrics = UiMetrics(
            token = 0f,
            euro = 0f,
            likes = 1,
            dislikes = 1,
            views = 1,
            comments = 1,
            report = 1
        )
        OverviewMetrics(metrics)
    }
}
