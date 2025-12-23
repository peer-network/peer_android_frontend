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
import eu.peernetwork.ads.ui.model.UiCharge
import eu.peernetwork.core.ui.theme.DesignTheme
import java.math.BigDecimal

@Composable
fun CheckoutFees(
    charge: UiCharge,
    price: BigDecimal,
    modifier: Modifier = Modifier
) {
    val tax = charge.percentage * price.toDouble()
    val peerFee = charge.peer * price.toDouble()
    val burn = charge.burn * price.toDouble()
    val taxPercent = (charge.percentage * 100).toInt()
    val peerPercent = (charge.peer * 100).toInt()
    val burnPercent = (charge.burn * 100).toInt()
    Column(modifier = modifier) {
        CheckoutFees(
            label = stringResource(R.string.peer_fee_label, peerPercent),
            value = peerFee.toString()
        )
        if (charge.percentage > 0) {
            CheckoutFees(
                label = stringResource(R.string.invitation_fee_label, taxPercent),
                value = tax.toString()
            )
        }
        CheckoutFees(
            label = stringResource(R.string.burn_label, burnPercent),
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
            price = BigDecimal.TEN,
            charge = UiCharge(
                percentage = 0.1,
                peer = 0.05,
                burn = 0.02,
                pool = 0.1
            )
        )
    }
}
