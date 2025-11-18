package eu.peernetwork.ads.ui.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun OverviewPage() {
    Column(modifier = Modifier.fillMaxSize()
        .padding(horizontal = 16.dp)
        .padding(vertical = 12.dp)) {
        OverviewLabel(
            label = "Earnings",
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 10.dp)
        ) { Text("12") }
        OverviewLabel(
            label = "Spendings",
            value = "12",
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Interactions",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(top = 18.dp)
                .padding(horizontal = 8.dp)
        )
        OverviewMetrics(Modifier.padding(top = 14.dp))
    }
}

@Preview
@Composable
fun PreviewOverviewPage() {
    DesignTheme(isDarkMode = true) {
        OverviewPage()
    }
}
