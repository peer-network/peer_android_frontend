package eu.peernetwork.wallet.ui.transactions

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.wallet.ui.R

@Composable
fun TransactionsSummeryItem(
    title: String,
    price: String,
    modifier: Modifier = Modifier,
    priceColor: Color? = null,
    color: Color = priceColor ?: MaterialTheme.colorScheme.outline,
    style: TextStyle = MaterialTheme.typography.bodySmall
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = style,
            modifier = Modifier.weight(1f),
            color = color
        )
        Text(
            text = price,
            style = style,
            color = priceColor ?: MaterialTheme.colorScheme.onBackground,
            fontWeight = priceColor?.let { FontWeight.Medium } ?: FontWeight.SemiBold
        )
        Icon(
            painter = painterResource(R.drawable.ic_peer_token),
            contentDescription = stringResource(R.string.token_label),
            tint = priceColor ?: MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 2.dp)
                .size(16.dp)
        )
    }
}

@Composable
@Preview
fun PreviewTransactionsSummery() {
    DesignTheme(isDarkMode = true) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TransactionsSummeryItem(
                title = stringResource(R.string.transaction_amount_label),
                price = "96",
            )
            TransactionsSummeryItem(
                title = stringResource(R.string.transaction_base_label),
                style = MaterialTheme.typography.labelSmall,
                price = "2",
                priceColor = MaterialTheme.colorScheme.outline
            )
        }
    }
}
