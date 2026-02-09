package eu.peernetwork.wallet.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.wallet.ui.R

@Composable
fun TransactionsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_transaction),
            contentDescription = stringResource(R.string.transfer_label),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 4.dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp)
        )
        Text(
            text = stringResource(R.string.transfer_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 10.dp)
                .weight(1f)
        )
        Icon(
            painter = painterResource(R.drawable.ic_proceed),
            contentDescription = stringResource(R.string.transfer_label),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 4.dp)
                .size(36.dp)
                .clip(CircleShape)
                .padding(6.dp)
        )
    }
}

@Preview
@Composable
fun PreviewTransactionsButton() {
    DesignTheme(isDarkMode = true) {
        TransactionsButton {}
    }
}
