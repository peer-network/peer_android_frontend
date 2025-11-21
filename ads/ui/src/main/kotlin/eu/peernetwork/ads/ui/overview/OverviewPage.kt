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
import eu.peernetwork.ads.domain.model.Metrics
import eu.peernetwork.ads.ui.R
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun OverviewPage(metrics: Metrics) {
    Column(modifier = Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .padding(vertical = 12.dp)) {
        OverviewLabel(
            label = stringResource(R.string.earning_label),
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 10.dp)
        ) { Text(metrics.euro.toString()) }
        OverviewLabel(
            label = stringResource(R.string.spending_label),
            value = metrics.token.toString(),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.interactions_label),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(top = 16.dp)
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
fun PreviewOverviewPage() {
    DesignTheme(isDarkMode = true) {
        val metrics = Metrics(
            token = 0f,
            euro = 0f,
            likes = 1,
            dislikes = 1,
            views = 1,
            comments = 1,
            report = 1
        )
        OverviewPage(metrics)
    }
}
