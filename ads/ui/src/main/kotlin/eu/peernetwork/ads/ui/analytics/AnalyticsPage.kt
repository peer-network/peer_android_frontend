package eu.peernetwork.ads.ui.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.ads.ui.R
import eu.peernetwork.ads.ui.model.UiMetrics
import eu.peernetwork.ads.ui.model.UiStatus
import eu.peernetwork.ads.ui.overview.OverviewStatistics
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun AnalyticsPage(
    title: AnnotatedString,
    description: AnnotatedString,
    from: String,
    to: String,
    start: String,
    end: String,
    status: Boolean,
    isAccessible: Boolean,
    metrics: UiMetrics,
    modifier: Modifier = Modifier,
    onClick: (DesignRichText, String) -> Unit,
    label: (@Composable () -> Unit)?,
    content: @Composable () -> Unit
) {
    val updatedLabel by rememberUpdatedState(label)
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp)
            .padding(bottom = 4.dp)
            .then(modifier)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.article_statistics_label),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        updatedLabel?.let {
            Box(
                modifier = Modifier.padding(top = 10.dp)
            ) { it() }
        }
        if (!isAccessible) {
            AnalyticsMask(
                status = status,
                modifier = Modifier.padding(vertical = 10.dp),
            ) {}
        } else {
            AnalyticsPost(
                title = title,
                description = description,
                status = status,
                modifier = Modifier.padding(vertical = 10.dp),
                onClick = onClick,
                content = content
            )
        }
        OverviewStatistics(
            metrics = metrics,
            modifier = Modifier.fillMaxSize()
                .padding(bottom = 12.dp)
        )
        AnalyticsItem(
            label = stringResource(R.string.start_at_label),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "$from $start",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        AnalyticsItem(
            label = stringResource(R.string.stop_at_label),
            modifier = Modifier.padding(top = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "$to $end",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        AnalyticsItem(
            label = stringResource(R.string.advert_total_label),
            value = metrics.token.toString(),
            modifier = Modifier.padding(top = 10.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun PreviewAnalyticsPage() {
    DesignTheme(isDarkMode = true) {
        val metrics = UiMetrics(
            token = 0f,
            euro = 0f,
            likes = "1",
            dislikes = "1",
            views = "1",
            comments = "1",
            report = "1"
        )
        AnalyticsPage(
            title = buildAnnotatedString { append("Title") },
            description = buildAnnotatedString { append("There’s something about hiking that resets everything. ") },
            status = true,
            isAccessible = true,
            from = "8 Jun 2025",
            to = "8 Jun 2025",
            start = "14:23",
            end = "14:23",
            metrics = metrics,
            onClick = { _,_ -> },
            label = { AnalyticsVisibilityLabel(UiStatus.HIDDEN) },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                .padding(12.dp),
        ) {}
    }
}
