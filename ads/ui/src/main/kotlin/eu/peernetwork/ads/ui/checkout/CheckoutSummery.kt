package eu.peernetwork.ads.ui.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.ads.ui.R
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun CheckoutSummery(modifier: Modifier = Modifier) {
    val border = MaterialTheme.colorScheme.surfaceContainerLow
    Column(
        modifier = Modifier.fillMaxWidth()
            .then(modifier)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 10.dp)
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    drawLine(
                        color = border,
                        start = Offset(0f, this.size.height),
                        end = Offset(this.size.width, this.size.height),
                        strokeWidth = strokeWidth
                    )
                }.padding(vertical = 14.dp)
        ) {
            Text(
                text = stringResource(R.string.promotion_cost_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "200",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Icon(
                    painter = painterResource(R.drawable.ic_icon),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 4.dp)
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(top = 16.dp)
                .padding(bottom = 6.dp)
        ) {
            Text(
                text = stringResource(R.string.promotion_fee_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        CheckoutFees(
            charges = 2,
            commission = 5,
            burn = 3,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewCheckoutSummery() {
    DesignTheme(isDarkMode = true) {
        CheckoutSummery()
    }
}
