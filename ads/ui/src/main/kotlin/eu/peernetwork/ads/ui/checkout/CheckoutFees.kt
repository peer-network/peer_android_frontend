package eu.peernetwork.ads.ui.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import eu.peernetwork.ads.ui.R
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun CheckoutFees(
    charges: Int,
    burn: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        CheckoutFees(
            label = stringResource(R.string.peer_fee_label),
            value = charges.toString()
        )
        CheckoutFees(
            label = stringResource(R.string.burn_label),
            value = burn.toString()
        )
    }
}

@Composable
fun CheckoutFees(
    label: String,
    value: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                painter = painterResource(R.drawable.ic_icon),
                contentDescription = null,
                modifier = Modifier.padding(start = 4.dp)
                    .size(16.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
fun PreviewCheckoutFees() {
    DesignTheme(isDarkMode = true) {
        CheckoutFees(
            charges = 2,
            burn = 3
        )
    }
}
