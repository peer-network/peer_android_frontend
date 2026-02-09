package eu.peernetwork.wallet.ui.confirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.wallet.ui.R

@Composable
fun ConfirmationPricing(
    label: String,
    price: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodyMedium
        )
        Box(modifier = Modifier.weight(1f)
            .height(1.dp)
            .padding(horizontal = 8.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest))
        Text(
            text = price,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Icon(
            painter = painterResource(R.drawable.ic_peer_token),
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview
@Composable
fun PreviewConfirmationPricing() {
    DesignTheme(isDarkMode = true) {
        ConfirmationPricing(
            label = stringResource(R.string.like_price_label),
            price = "100",
            modifier = Modifier.padding(16.dp)
        )
    }
}
