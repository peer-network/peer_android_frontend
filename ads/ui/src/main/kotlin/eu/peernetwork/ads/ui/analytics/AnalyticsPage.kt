package eu.peernetwork.ads.ui.analytics

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
import eu.peernetwork.ads.ui.quote.QuoteLabel
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun AnalyticsPage(
    title: AnnotatedString,
    description: AnnotatedString,
    modifier: Modifier = Modifier,
    media: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 16.dp)
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
        AnalyticsSummery(
            title = title,
            description = description,
            modifier = Modifier.padding(vertical = 12.dp),
            content = media
        )
        updatedContent()
        QuoteLabel(
            label = stringResource(R.string.start_label),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.advert_start),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        QuoteLabel(
            label = stringResource(R.string.start_label),
            modifier = Modifier.padding(top = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.advert_start),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        AnalyticsLabel(
            label = "Spendings",
            value = "12",
            modifier = Modifier.padding(top = 10.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun PreviewAnalyticsPage() {
    DesignTheme(isDarkMode = true) {
        AnalyticsPage(
            title = buildAnnotatedString { append("Title") },
            description = buildAnnotatedString { append("There’s something about hiking that resets everything. ") },
            modifier = Modifier.padding(12.dp),
            media = {}
        ) {}
    }
}
