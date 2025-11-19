package eu.peernetwork.ads.ui.quote

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.ads.ui.R
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignOutlineButton
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun QuotePage(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .then(modifier)
    ) {
        Text(
            text = stringResource(R.string.period_label),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        QuoteLabel(
            label = "Start",
            value = "12",
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        )
        QuoteLabel(
            label = "Duration",
            value = "12",
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        )
        QuoteLabel(
            label = "Shown",
            value = "12",
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        )
        Text(
            text = "Billing summary",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 16.dp)
        )
        QuoteLabel(
            label = "Total ad cost",
            value = "12",
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DesignOutlineButton(
                onClick = onBack,
                minHeight = 48.dp,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.weight(1f),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )
            ) { Text(stringResource(R.string.back_label)) }
            DesignButton(
                onClick = onNext,
                minHeight = 48.dp,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.weight(1f),
            ) { Text(stringResource(R.string.next_label)) }
        }
    }
}

@Preview
@Composable
fun PreviewArticlePage() {
    DesignTheme(isDarkMode = true) {
        QuotePage(
            modifier = Modifier.padding(vertical = 16.dp),
            onBack = {}
        ) {}
    }
}
