package eu.peernetwork.ads.ui.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.ads.ui.R
import eu.peernetwork.ads.ui.model.UiMetrics
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun OverviewStatistics(
    metrics: UiMetrics,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OverviewLabel(
            label = stringResource(R.string.earning_label),
            modifier = Modifier.fillMaxWidth()
        ) { Text(metrics.euro.toString()) }
        Text(
            text = stringResource(R.string.interactions_label),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(top = 12.dp)
                .padding(horizontal = 8.dp)
        )
        OverviewMetrics(
            metrics = metrics,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Preview
@Composable
fun PreviewOverviewStatistics() {
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
        OverviewStatistics(
            metrics = metrics,
            modifier = Modifier.fillMaxSize()
                .padding(12.dp)
        )
    }
}
