package eu.peernetwork.wallet.ui.confirmation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignOutlineButton
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.wallet.ui.R

@Composable
fun ConfirmationPage(
    title: String,
    painter: Painter,
    label: String,
    price: String,
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(modifier = modifier) {
        ConfirmationTitle(
            title = title,
            painter = painter,
        )
        ConfirmationPricing(
            label = label,
            price = price,
            modifier = Modifier.padding(top = 12.dp)
                .padding(end = 16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 20.dp)
                .padding(bottom = 6.dp)
        ) {
            DesignOutlineButton(
                onClick = onCancel,
                minHeight = 42.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.cancel_label)) }
            DesignButton(
                onClick = onConfirm,
                minHeight = 42.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                colors = designSecondaryButtonColors(),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.confirm_label)) }
        }
    }
}

@Preview
@Composable
fun PreviewConfirmationPage() {
    DesignTheme(isDarkMode = true) {
        ConfirmationPage(
            painter = painterResource(R.drawable.ic_love),
            title = stringResource(R.string.no_like_caption),
            label = stringResource(R.string.like_price_label),
            price = "100",
            modifier = Modifier.padding(16.dp),
            onCancel = {}
        ) {}
    }
}
